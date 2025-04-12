from datetime import datetime
from typing import List, Optional
from pydantic import BaseModel


class MediaContent(BaseModel):
    id: int
    dataType: str
    dataContent: str
    userId: str
    checksum: Optional[str] = None
    llmDescription: Optional[str] = None
    llmModerationStatus: Optional[str] = None
    rejectionReason: Optional[str] = None
    tags: Optional[str] = None
    fileSize: Optional[str] = None
    metadata: Optional[str] = None
    status: Optional[str] = None
    createdAt: datetime
    updatedAt: datetime


class DataItem(BaseModel):
    id: int
    mediaContentIdList: List[int]
    likesCount: int
    dislikesCount: int
    tags: Optional[str] = None
    createdAt: datetime
    updatedAt: datetime
    mediaContentList: List[MediaContent]


class ResponseModel(BaseModel):
    status: int
    message: str
    data: List[DataItem]
    timestamp: int
