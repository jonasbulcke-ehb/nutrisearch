package be.ehb.gdt.nutrisearch.excel.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class InvalidExcelException : RuntimeException("The provided excel cannot be used to import products")
