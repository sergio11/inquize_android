package com.dreamsoftware.inquize.domain.validation

interface IBusinessEntityValidator<T> {
    fun validate(entity: T): Map<String, String>
}