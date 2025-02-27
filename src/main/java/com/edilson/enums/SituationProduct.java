package com.edilson.enums;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(description = "Situação do Produto")
public enum SituationProduct {
    Ativo,
    Inativo
}
