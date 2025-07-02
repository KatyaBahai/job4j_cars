package ru.job4j.cars.service.brand;

import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;
import ru.job4j.cars.model.Brand;
import ru.job4j.cars.repository.brand.BrandRepository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class SimpleBrandService implements BrandService {
    private final BrandRepository brandRepository;
    private final SessionFactory sf;

    @Override
    public Collection<Brand> findAll() {
        Session session = sf.getCurrentSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Collection<Brand> brands = brandRepository.findAll();
            transaction.commit();
            return brands;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }

    @Override
    public Optional<Brand> findById(int id) {
        return brandRepository.findById(id);
    }
}
