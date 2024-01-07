package org.user_service.advice;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.ArrayList;

@Component
@RequestScope
public class ErrorList extends ArrayList<String> {
}