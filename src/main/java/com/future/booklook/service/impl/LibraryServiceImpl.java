package com.future.booklook.service.impl;

import com.future.booklook.repository.LibraryRepository;
import com.future.booklook.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LibraryServiceImpl implements LibraryService {
    @Autowired
    private LibraryRepository libraryRepository;
}