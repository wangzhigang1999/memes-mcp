from functools import wraps

import requests
from fastapi import HTTPException
from pydantic import ValidationError


def handle_errors(func):
    @wraps(func)
    async def wrapper(*args, **kwargs):
        try:
            return await func(*args, **kwargs)
        except ValidationError as e:
            raise HTTPException(
                status_code=502, detail=f"Invalid API response: {str(e)}"
            )
        except ValueError as e:
            raise HTTPException(status_code=400, detail=str(e))
        except requests.RequestException as e:
            raise HTTPException(status_code=502, detail=f"API request failed: {str(e)}")
        except Exception as e:
            raise HTTPException(status_code=500, detail=f"Unexpected error: {str(e)}")

    return wrapper
