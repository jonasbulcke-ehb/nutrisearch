package be.ehb.gdt.nutrisearch.domain.exceptions

import org.bson.types.ObjectId
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class ResourceNotFoundException(val type: String, val id: String) : RuntimeException() {
    override val message: String
        get() = "Resource of type $type with id $id could not be found"

    constructor(typeClass: Class<*>, id: String) : this(typeClass.simpleName, id)
    constructor(type: String, id: ObjectId): this(type, id.toHexString())
    constructor(typeClass: Class<*>, id: ObjectId): this(typeClass.simpleName, id.toHexString())
}