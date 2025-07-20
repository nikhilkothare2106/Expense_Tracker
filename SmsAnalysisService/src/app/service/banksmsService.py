from typing import Optional
import os
from dotenv import load_dotenv
from langchain_core.prompts import ChatPromptTemplate
from langchain_mistralai import ChatMistralAI
from pydantic import BaseModel, SecretStr
import re

class BANKVERIFY:
    def __init__(self):
        load_dotenv()
        api_key_value = os.getenv("MISTRAL_API_KEY")
        if not api_key_value:
            raise ValueError("MISTRAL_API_KEY is not set in environment variables.")

        # More strict prompt with output format enforcement
        self.prompt = ChatPromptTemplate.from_template(
            """You are a banking message classifier. Analyze this message and respond STRICTLY with either 'true' or 'false':

            Message: {text}

            Classification Rules:
            1. Respond 'true' ONLY if:
                - It's a genuine banking transaction (debits, credits, UPI, etc.)
                - AND not marked as 'test', 'simulated', 'demo', or 'example'
            2. Respond 'false' if:
                - Non-banking message
                - Test/simulated transaction
                - Example/demo message
                - Any other case
            
            IMPORTANT:
            - Your response must be EXACTLY 'true' or 'false'
            - No explanations, no additional text
            - No punctuation or formatting"""
        )

        self.llm = ChatMistralAI(api_key=SecretStr(api_key_value), temperature=0)

    def runLLM(self, message: str) -> bool:
        try:
            response = self.llm.invoke(self.prompt.format(text=message))
            cleaned_response = response.content.strip().lower()  # type: ignore
            
            # Extract first true/false match using regex
            match = re.search(r'(true|false)', cleaned_response)
            if match:
                return match.group(0) == 'true'
            
            print(f"Invalid response format: {response.content}")
            return False
                
        except Exception as e:
            print(f"Classification error: {str(e)}")
            return False