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
    private final PatientsRepository patientsRepository;
    private final DakterisRepository drRepository;
    public CrmService(ContactRepository contactRepository,
                      CompanyRepository companyRepository,
                      StatusRepository statusRepository,
                      KolposkopijaIzmeklejumsRepository kolposkopijaIzmeklejumsRepository,
                      PatientsRepository patientsRepository,
                      DakterisRepository drRepository  ) {
        this.contactRepository = contactRepository;
        this.companyRepository = companyRepository;
        this.statusRepository = statusRepository;
        this.kolposkopijaIzmeklejumsRepository = kolposkopijaIzmeklejumsRepository;
        this.patientsRepository = patientsRepository;
        this.drRepository = drRepository;
    }

    public List<Contact> findAllContacts(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return contactRepository.findAll();
        } else {
            return contactRepository.search(stringFilter);
        }
    }
    public List<PacientsEntity> findAllPatients(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return patientsRepository.findAll();
        } else {
            return patientsRepository.search(stringFilter);
        }
    }
    public List<DakterisEntity> findAllDrs(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return drRepository.findAll();
        } else {
            return drRepository.search(stringFilter);
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
    public KolposkopijaIzmeklejumsEntity saveVisit(KolposkopijaIzmeklejumsEntity entity) {
        if (entity == null) {
            System.err.println("Visit data is null. Are you sure you have connected your form to the application?");
            return null;
        }
        return kolposkopijaIzmeklejumsRepository.save(entity);
    }
    public List<Company> findAllCompanies() {
        return companyRepository.findAll();
    }

    public List<Status> findAllStatuses(){
        return statusRepository.findAll();
    }
}
