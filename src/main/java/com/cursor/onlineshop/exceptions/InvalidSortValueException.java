package com.cursor.onlineshop.exceptions;

public class InvalidSortValueException extends Throwable {

    private static final String MESSAGE =
            "Values for parameter \"sortBy\" must be: \"name\", \"category\", \"price_asc\" or \"price_desc\"";

    public InvalidSortValueException() {
        super(MESSAGE);
    }
}
