package br.com.book.api.adapter.out.persistence.mapper;

import br.com.book.api.adapter.out.persistence.entity.LoanEntity;
import br.com.book.api.domain.Loan;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class LoanMapper implements Serializable {


    @Inject
    ModelMapper modelMapper;

    @Inject
    BookMapper bookMapper;

    @Inject
    UserMapper userMapper;

    // Domain → Entity (com relacionamentos)
    public LoanEntity toEntity(Loan loan) {
        if (loan == null) return null;

        LoanEntity entity = modelMapper.map(loan, LoanEntity.class);

        // Mapear relacionamentos manualmente (porque são objetos complexos)
        if (loan.getBook() != null) {
            entity.setBook(bookMapper.toEntity(loan.getBook()));
        }
        if (loan.getUser() != null) {
            entity.setUser(userMapper.toEntity(loan.getUser()));
        }

        return entity;
    }

    // Entity → Domain (com relacionamentos)
    public Loan toDomain(LoanEntity entity) {
        if (entity == null) return null;

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Loan loan = modelMapper.map(entity, Loan.class);

        // Mapear relacionamentos manualmente
        if (entity.getBook() != null) {
            loan.setBook(bookMapper.toDomain(entity.getBook()));
        }
        if (entity.getUser() != null) {
            loan.setUser(userMapper.toDomain(entity.getUser()));
        }

        return loan;
    }

    // Lista Domain → Lista Entity
    public List<LoanEntity> toEntityList(List<Loan> loans) {
        if (loans == null) return List.of();
        return loans.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    // Lista Entity → Lista Domain
    public List<Loan> toDomainList(List<LoanEntity> entities) {
        if (entities == null) return List.of();
        return entities.stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }
}