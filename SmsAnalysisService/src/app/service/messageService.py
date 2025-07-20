
from app.service.llmService import LLMService
from app.service.banksmsService import BANKVERIFY

class MessageService:
  def __init__(self):
    self.messageUtil = BANKVERIFY()
    self.llmService = LLMService()

  def process_message(self,message):
    if(self.messageUtil.runLLM(message)):
      return self.llmService.runLLM(message)
    else:
      return None