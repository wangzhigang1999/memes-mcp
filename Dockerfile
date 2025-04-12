# 第一阶段：构建环境
FROM python:3.10-slim as builder

WORKDIR /app

# 创建虚拟环境
RUN python -m venv /opt/venv
ENV PATH="/opt/venv/bin:$PATH"

# 安装 Python 依赖
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt


# 第二阶段：运行环境
FROM python:3.10-slim

# 设置工作目录
WORKDIR /app

# 从构建阶段复制虚拟环境
COPY --from=builder /opt/venv /opt/venv

# 设置环境变量
ENV PATH="/opt/venv/bin:$PATH"
ENV PYTHONUNBUFFERED=1

# 复制应用代码
COPY . .

EXPOSE 8000

# 启动命令
CMD ["python", "app.py"]