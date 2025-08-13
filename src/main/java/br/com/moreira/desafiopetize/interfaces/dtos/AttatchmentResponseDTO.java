package br.com.moreira.desafiopetize.interfaces.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

public record AttatchmentResponseDTO(
        @Schema(description = "Attachment ID", example = "1")
        Long id,

        @Schema(description = "File original name", example = "receipt.pdf")
        String fileName,

        @Schema(description = "File type", example = "application/pdf")
        String fileType,

        @Schema(description = "File size (bytes)", example = "102400")
        long size
) {
}
