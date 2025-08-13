package br.com.moreira.desafiopetize.domain.services.mapper;

import br.com.moreira.desafiopetize.domain.entities.Attachment;
import br.com.moreira.desafiopetize.interfaces.dtos.AttatchmentResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class AttachmentMapper {

    public AttatchmentResponseDTO toResponseDTO(Attachment entity) {
        if (entity == null) {
            return null;
        }

        return new AttatchmentResponseDTO(
                entity.getId(),
                entity.getFileName(),
                entity.getFileType(),
                entity.getSize()
        );
    }
}
