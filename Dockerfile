# syntax=docker/dockerfile:1.4

FROM nginx:latest
COPY ./webpagedata /usr/share/nginx/html


