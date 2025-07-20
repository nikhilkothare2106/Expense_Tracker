from flask import Flask, request, jsonify
from .service.messageService import MessageService
from kafka import KafkaProducer
from dotenv import load_dotenv
import json
import logging
import atexit
import os

app = Flask(__name__)
app.config.from_pyfile('config.py')

# Configure logging
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

# Initialize MessageService
messageService = MessageService()

# Initialize Kafka Producer (singleton pattern)
_kafka_producer = None

def get_kafka_producer():
    global _kafka_producer
    if _kafka_producer is None:
        try:
            _kafka_producer = KafkaProducer(
                bootstrap_servers=os.getenv("KAFKA_BOOTSTRAP_SERVERS"),
                value_serializer=lambda v: json.dumps(v).encode('utf-8'),
                security_protocol=app.config.get('KAFKA_SECURITY_PROTOCOL', 'PLAINTEXT'),
                ssl_cafile=app.config.get('KAFKA_SSL_CAFILE'),
                ssl_certfile=app.config.get('KAFKA_SSL_CERTFILE'),
                ssl_keyfile=app.config.get('KAFKA_SSL_KEYFILE'),
                # Additional settings for better stability
                retries=5,
                reconnect_backoff_ms=1000,
                max_in_flight_requests_per_connection=1
            )
            logger.info("Kafka producer initialized successfully")
        except Exception as e:
            logger.error(f"Failed to create Kafka producer: {e}")
            raise
    return _kafka_producer

def close_kafka_producer():
    global _kafka_producer
    if _kafka_producer is not None:
        _kafka_producer.close()
        _kafka_producer = None
        logger.info("Kafka producer closed")

# Register the close function to be called at program exit
atexit.register(close_kafka_producer)

KAFKA_TOPIC = os.getenv("KAFKA_TOPIC")

@app.route('/ds/message', methods=['POST'])
def handle_message():
    if request.json is None:
        return jsonify({'error': 'Invalid or missing JSON in request'}), 400
    
    message = request.json.get('message')
    if not message:
        return jsonify({'error': 'Message field is required'}), 400
    
    try:
        
        result = messageService.process_message(message)

        producer = get_kafka_producer()
        
        future = producer.send(KAFKA_TOPIC, value=result)
                
        logger.info(f"Result sent to Kafka topic {KAFKA_TOPIC}")
        
        return jsonify({'result': result})
    
    except Exception as e:
        logger.error(f"Error processing message: {e}")
        return jsonify({'error': 'Internal server error'}), 500

@app.route('/', methods=['GET'])
def handle_get():
    return 'Hello world ...'

if __name__ == "__main__":
    # Pre-initialize the producer when starting the app
    get_kafka_producer()
    app.run(host="localhost", port=8001, debug=True)