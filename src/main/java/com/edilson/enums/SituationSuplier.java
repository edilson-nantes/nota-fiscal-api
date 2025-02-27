package com.edilson.enums;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(description = "Situação do Fornecedor")
public enum SituationSuplier {
    Ativo,
    Baixado,
    Suspenso
}
