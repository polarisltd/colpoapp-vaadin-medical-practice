package com.example.application.services;

import com.example.application.data.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CrmService {

    private final ContactRepository contactRepository;
    private final CompanyRepository companyRepository;
    private final StatusRepository statusRepository;
    private final KolposkopijaIzmeklejumsRepository kolposkopijaIzmeklejumsRepository;

    public CrmService(ContactRepository contactRepository,
                      CompanyRepository companyRepository,
                      StatusRepository statusRepository,
                      KolposkopijaIzmeklejumsRepository kolposkopijaIzmeklejumsRepository) {
        this.contactRepository = contactRepository;
        this.companyRepository = companyRepository;
        this.statusRepository = statusRepository;
        this.kolposkopijaIzmeklejumsRepository = kolposkopijaIzmeklejumsRepository;
    }

    public List<Contact> findAllContacts(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return contactRepository.findAll();
        } else {
            return contactRepository.search(stringFilter);
        }
    }

    public long countContacts() {
        return contactRepository.count();
    }

    public void deleteContact(Contact contact) {
        contactRepository.delete(contact);
    }

    public void saveContact(Contact contact) {
        if (contact == null) {
            System.err.println("Contact is null. Are you sure you have connected your form to the application?");
            return;
        }
        contactRepository.save(contact);
    }
    public void saveVisit(KolposkopijaIzmeklejumsEntity entity) {
        if (entity == null) {
            System.err.println("Visit data is null. Are you sure you have connected your form to the application?");
            return;
        }
        kolposkopijaIzmeklejumsRepository.save(entity);
    }
    public List<Company> findAllCompanies() {
        return companyRepository.findAll();
    }

    public List<Status> findAllStatuses(){
        return statusRepository.findAll();
    }
}
