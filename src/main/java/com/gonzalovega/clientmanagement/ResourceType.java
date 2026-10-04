package com.gonzalovega.clientmanagement;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(enumAsRef = true)
public enum ResourceType {
    APPLICATION,
    SERVICE
}
