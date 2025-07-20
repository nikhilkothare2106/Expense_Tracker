from typing import Optional
from pydantic import BaseModel, Field

class Expense(BaseModel):
    amount: Optional[str] = Field(
        default=None,
        description="Monetary amount spent in the transaction, e.g., '1250.00'"
    )
    currency: Optional[str] = Field(
        default=None,
        description="Currency used, e.g., 'INR', 'USD'"
    )
    merchant: Optional[str] = Field(
        default=None,
        description="Name of the merchant/store, e.g., 'Amazon', 'Flipkart'"
    )
