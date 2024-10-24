package com.xm.crypto.recommendation.controller.command;

public interface Command<RequestT, ResponseT> {
    ResponseT execute(RequestT requestDto);
}
