package com.serviciosyave.entities;


import jakarta.persistence.*;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "sellers")
public class Seller {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Paso 1: Información Personal
    private String fullName;
    private int age;
    private LocalDate birthdate;
    private String idNumber;
    private String gender;
    private String city;

    // Paso 2: Información Profesional
    private String profession;
    private int yearsOfExperience;
    private String skillsDescription;

    @ElementCollection
    private List<String> modalities; // ["presencial", "online", "hibrido"]

    // Paso 3: Verificación
    private String dniFrontName;
    private String dniBackName;
    private String selfieName;
    private String universityTitleName;

    @ElementCollection
    private List<String> certificationsNames;

    // Paso 4: Galería
    @ElementCollection
    private List<String> galleryImagesNames;

    // Relación con el usuario
    @Column(name = "user_id", unique = true)
    private Long userId;

    // Campos adicionales
    private LocalDateTime createdAt;

    public Seller() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public int getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(int yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public String getSkillsDescription() {
        return skillsDescription;
    }

    public void setSkillsDescription(String skillsDescription) {
        this.skillsDescription = skillsDescription;
    }

    public List<String> getModalities() {
        return modalities;
    }

    public void setModalities(List<String> modalities) {
        this.modalities = modalities;
    }

    public String getDniFrontName() {
        return dniFrontName;
    }

    public void setDniFrontName(String dniFrontName) {
        this.dniFrontName = dniFrontName;
    }

    public String getDniBackName() {
        return dniBackName;
    }

    public void setDniBackName(String dniBackName) {
        this.dniBackName = dniBackName;
    }

    public String getSelfieName() {
        return selfieName;
    }

    public void setSelfieName(String selfieName) {
        this.selfieName = selfieName;
    }

    public String getUniversityTitleName() {
        return universityTitleName;
    }

    public void setUniversityTitleName(String universityTitleName) {
        this.universityTitleName = universityTitleName;
    }

    public List<String> getCertificationsNames() {
        return certificationsNames;
    }

    public void setCertificationsNames(List<String> certificationsNames) {
        this.certificationsNames = certificationsNames;
    }

    public List<String> getGalleryImagesNames() {
        return galleryImagesNames;
    }

    public void setGalleryImagesNames(List<String> galleryImagesNames) {
        this.galleryImagesNames = galleryImagesNames;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

