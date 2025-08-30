FROM python:latest

# set workdir 
WORKDIR /app

# install dependencies
COPY ./requirements.txt /app
RUN pip install --no-cache-dir --upgrade -r requirements.txt

# copy scripts to the folder
COPY . /app

# start the server
CMD ["python", "./main.py"]
