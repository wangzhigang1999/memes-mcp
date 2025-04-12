from typing import List
from urllib.parse import urljoin

import requests
from mcp.server.fastmcp import FastMCP
from pydantic import ValidationError

from model import ResponseModel

BASE_URL = "https://api.memes.bupt.site/api/"
mcp = FastMCP("memes")


@mcp.tool()
def get_meme(limit: int = 20) -> List[str]:
    """
    Get random memes from the memes.bupt.site.

    Args:
        limit: Number of memes to retrieve (1-50)

    Returns:
        List of meme image URLs

    Raises:
        ValueError: If limit is out of range
        requests.RequestException: If API request fails
        ValidationError: If API response is invalid
    """
    if not 1 <= limit <= 50:
        raise ValueError("limit must be between 1 and 50")
    try:
        response = requests.get(
            url=urljoin(BASE_URL, "submission"),
            params={
                'pageSize': str(limit),
                'random': 'true'
            },
            headers={
                'uuid': 'mcp-server'
            },
            timeout=10
        )
        response.raise_for_status()

        response_model = ResponseModel(**response.json())

        return [
            item.mediaContentList[0].dataContent
            for item in response_model.data
            if (len(item.mediaContentIdList) == 1 and item.mediaContentList[0].dataType == 'IMAGE')
        ]

    except requests.RequestException as e:
        raise requests.RequestException(f"API request failed: {str(e)}")
    except ValidationError as e:
        raise ValidationError(f"Invalid API response: {str(e)}")


if __name__ == "__main__":
    mcp.run(transport="sse")
