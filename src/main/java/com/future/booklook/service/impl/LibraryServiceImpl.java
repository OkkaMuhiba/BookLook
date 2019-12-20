package com.future.booklook.service.impl;

import com.future.booklook.model.entity.Library;
import com.future.booklook.model.entity.Product;
import com.future.booklook.model.entity.User;
import com.future.booklook.repository.LibraryRepository;
import com.future.booklook.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class LibraryServiceImpl implements LibraryService {
    @Autowired
    private LibraryRepository libraryRepository;

    public Library save(Library library){
        return libraryRepository.save(library);
    }

    public Boolean existsByUserAndProduct(User user, Product product){
        return libraryRepository.existsByUserAndProduct(user, product);
    }

    public Set<Library> findAllByUser(User user){
        return libraryRepository.findAllByUser(user);
    }
}
