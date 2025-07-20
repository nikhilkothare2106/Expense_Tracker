from typing import Optional
import os
from dotenv import load_dotenv
from langchain_core.prompts import ChatPromptTemplate
from langchain_mistralai import ChatMistralAI

class LLMService:
    def __init__(self):
        load_dotenv()
        api_key_value = os.getenv("MISTRAL_API_KEY")
        if not api_key_value:
            raise ValueError("MISTRAL_API_KEY is not set in environment variables.")

        # Use string API key
        self.llm = ChatMistralAI(api_key=api_key_value, temperature=0) # type: ignore

        # Using few-shot + JSON-style prompt
        self.prompt = ChatPromptTemplate.from_messages(
            [
                (
                    "system",
                    "You are an expert at extracting structured data from financial messages. "
                    "Extract amount, currency, and merchant as JSON. Return 'null' for any missing fields."
                ),
                (
                    "human", 
                    "Example 1:\n"
                    "Input: 'INR 1,250.00 was spent using your Debit Card at AMAZON INDIA on 16-Jul-2025 14:18 IST.'\n"
                    "Output: {{\"amount\": \"1250.00\", \"currency\": \"INR\", \"merchant\": \"AMAZON INDIA\"}}\n\n"
                    "Now extract from:\n{text}"
                )
            ]
        )


    def runLLM(self, message: str):
        chain = self.prompt | self.llm
        result = chain.invoke({"text": message})
        print("Raw model output:\n", result.content)
        return result.content
