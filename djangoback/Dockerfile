FROM python:3.6.8
RUN mkdir /django
WORKDIR /django

ADD requirements.txt /django/
RUN pip install -r requirements.txt
ADD . /django/