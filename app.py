from typing import List
from urllib.parse import urljoin

import requests
from mcp.server.fastmcp import FastMCP

from error import handle_errors
from model import ResponseModel

BASE_URL = "https://api.memes.bupt.site/api/"
mcp = FastMCP("memes")


@mcp.tool()
@handle_errors
async def get_meme(limit: int = 20) -> List[str]:
    """
    Get random memes from the memes.bupt.site.

    Args:
        limit: Number of memes to retrieve (1-50)

    Returns:
        List of meme image URLs
    """
    if not 1 <= limit <= 50:
        raise ValueError("limit must be between 1 and 50")

    response = requests.get(
        url=urljoin(BASE_URL, "submission"),
        params={"pageSize": str(limit), "random": "true"},
        headers={"uuid": "mcp-server"},
        timeout=10,
    )
    response.raise_for_status()

    response_model = ResponseModel(**response.json())

    return [
        item.mediaContentList[0].dataContent
        for item in response_model.data
        if (
            len(item.mediaContentIdList) == 1
            and item.mediaContentList[0].dataType == "IMAGE"
        )
    ]


if __name__ == "__main__":
    mcp.run(transport="sse")
