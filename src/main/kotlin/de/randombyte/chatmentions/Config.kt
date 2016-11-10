package de.randombyte.chatmentions

import ninja.leaping.configurate.objectmapping.Setting
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable

@ConfigSerializable
class Config {
    @Setting val symbol: String = "@"
}