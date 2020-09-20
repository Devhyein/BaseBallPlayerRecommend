FROM python:3.6.8

RUN mkdir /code
WORKDIR /code

COPY . /code/
RUN pip install -r requirements.txt

EXPOSE 8000
ENTRYPOINT [ "python", "manage.py", "runserver" ]
