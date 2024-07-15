package com.casado.repository

case class  Client(id: Long, name: String)

case class Address(id: Long,
    outside: String,
    number: Long,
    city: String,
    state: String,
    zipCode: String,
    clientId: Long
)


