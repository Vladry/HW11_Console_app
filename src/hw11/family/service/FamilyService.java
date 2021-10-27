package hw11.family.service;

import hw11.family.Animals.*;
import hw11.family.DAO.FamilyDAO;
import hw11.family.People.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.time.LocalDate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.Integer.parseInt;

public class FamilyService implements Services {
    public FamilyDAO<Family> dao;

    public FamilyService(FamilyDAO<Family> dao) {
        this.dao = dao;
    }

    public static String getKeyboardInput(){
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String choice = null;
        try {
            choice = br.readLine();
        } catch (IOException e) {
            System.out.println("ошибка ввода br.readLine()");
        }
        return choice;
    }
    public boolean checkInputInt(int ind, int size){
        if (ind < 0 || ind > size) {
            System.out.println("incorrect number, returning to main menu");
            return false;
        }
        return true;
    }
    public int getIndex(String msg){
        System.out.println(msg);
        int familiesTotal = dao.getAllFamilies().size();
        System.out.println("available range: from 0 to " + (familiesTotal - 1));
        int famNumber = parseInt(getKeyboardInput());
        if (!checkInputInt(famNumber, familiesTotal) ) return -1;
        return famNumber;
    }

    public void register(int birthFamIndex, String bName, String gName){
        Family family = dao.getAllFamilies().get(birthFamIndex);
        Human newChild = BabyFactory.deliverABaby(family, bName, gName);
        assert newChild != null;
        family.setChildren(newChild);
        saveFam(family);
    };
    public void adopt(int adoptFamIndex, String name, String surname, String patrName, String birthDateRaw, int iq){
//  Family FamilyService.adoptChild(Family family, Human newBaby)

    };

    public Human createMember(Map<String, String> params, String member){
        if (member.equals("mom")) {
            return new Woman(params.get("name"), params.get("surname"),
                    params.get("year of birth (format: 'yyyy')"), params.get("month of birth (format: 'mm')"),
                    params.get("date of birth (format: 'dd')"), parseInt(params.get("iq")));
        } else if (member.equals("dad"))
        {
            return new Man(params.get("name"), params.get("last name"),
                    params.get("year of birth (format: 'yyyy')"), params.get("month of birth (format: 'mm')"),
                    params.get("date of birth (format: 'dd')"), parseInt(params.get("iq")));
        }
        return null;
    };

    public void displayAllFamilies() {
        System.out.println("all families to screen: ");
        dao.getAllFamilies().forEach(f-> System.out.println(f.prettyFormat() ) );
    }

    public Optional<List<Family>> getFamiliesBiggerThan(int num) {
        if (num <= 0) return Optional.empty();
        System.out.println("FamiliesBiggerThan " + num + " :");
        List<Family> listFam =
                dao.getAllFamilies().stream()
                        .filter(f -> f.getChildren().size() + 2 > num)
                        .collect(Collectors.toList());
        listFam.forEach(System.out::println);
        return Optional.of(listFam);
    }

    public Optional<List<Family>> getFamiliesLessThan(int num) {
        if (num <= 0) return Optional.empty();
        System.out.println("FamiliesLessThan " + num + " :");
        List<Family> listFam = dao.getAllFamilies().stream()
                .filter(f -> (f.getChildren().size() + 2 < num))
                .collect(Collectors.toList());
        listFam.stream().forEach(System.out::println);
        return Optional.of(listFam);
    }

    public int countFamiliesWithMemberNumber(int num) {
        if (num <= 1) return -1;
        System.out.println("now counting FamiliesWithMemberNumber " + num + " ....");
        return (int) dao.getAllFamilies().stream().filter(f -> f.getChildren().size() + 2 == num)
                .count();
    }

    public boolean createNewFamily(String dadName, String momName, String lastName,
                                   LocalDate dadBirthDate, LocalDate momBirthDate, int ownChildren, int adoptedChildren) {
        if (dadName == null || momName == null || ownChildren < 0 || adoptedChildren < 0) return false;
        Human dad = new Man(dadName, lastName, dadBirthDate);
        Human mom = new Woman(momName, lastName + "a", momBirthDate);
        Family family = new Family(dad, mom);
        dad.setFamily(family);
        mom.setFamily(family);
//        System.out.println("dad describeAge():  " + dad.describeAge()); //расчитать возраст человека

        for (int i = 0; i < ownChildren; i++) {
            bornChild(family, dadName, momName);
//            System.out.println("born a child: " + family.getChildren().get(i).toString());
        }

        for (int i = 0; i < adoptedChildren; i++) {
            adoptChild(family, new Man("fake", "fake", LocalDate.now()));
//            System.out.println("adopted a child: " + family.getChildren().get(ownChildren + i).toString());
        }
        dao.saveFamily(family);
        return true;
    }

    public void saveFam(Family f){
        dao.saveFamily(f);
    }

    public boolean deleteFamilyByIndex(int i) {
        dao.deleteFamily(i);
        return false;
    }

    public Family bornChild(Family family, String nameDad, String nameMon) {
        String babyName = "";
        Human newBaby = null;
        Sex sex;       // MASCULINE, FEMININE
        int rndSex, birthYear, birthMonth, birthDay;

        int babyIq = (family.getFather().getIq() + family.getMother().getIq()) / 2;

        Random random = new Random();
        rndSex = random.nextInt(2);
        birthYear = random.nextInt(10) + 2010;
        birthMonth = random.nextInt(12) + 1;
        birthDay = random.nextInt(28) + 1;
        sex = (rndSex == 0) ? Sex.MASCULINE : Sex.FEMININE;
        babyName = GenerateRandomName.get(sex);
        LocalDate birthDate = LocalDate.of(birthYear, birthMonth, birthDay);

        switch (sex) {
            case MASCULINE:
                newBaby = new Man("мальчик: " + babyName, family.getFather().getSurname(), birthDate, babyIq, family.getFather().getSchedule(), family);
                break;
            case FEMININE:
                newBaby = new Woman("девочка: " + babyName, family.getFather().getSurname() + "a", birthDate, babyIq, family.getFather().getSchedule(), family);
                break;
            default:
        }

        family.setChildren(newBaby);
        return family;
    }

    public Family adoptChild(Family family, Human newBaby) {
        bornChild(family, family.getFather().getName(), family.getMother().getName());
        return family;
    }


    public boolean deleteAllChildrenOlderThen(int age) {
        if (age <= 0) return false;
        List<Family> families = dao.getAllFamilies();
        int yearNow = LocalDate.now().getYear();
        IntStream.range(0, families.size()).forEach(famIndx ->
                 {
                    IntStream.range(0, families .get(famIndx).getChildren().size()  ).forEach(chidIndex ->{
                        int birthYear = families.get(famIndx).getChildren().get(chidIndex).getBirthDate().getYear();
                        if (yearNow - birthYear > age) {
                            System.out.println("this child is: " + (yearNow - birthYear) + " years old and must be deleted!");
                            System.out.println("deleting: " + families.get(famIndx).getChildren().get(chidIndex));
                            dao.deleteChild(famIndx, chidIndex);
                        }
                    }); //close chidIndex range
                } //close famIndx range
        );
        System.out.println("after removal of children aged over " + age + " years old: ");
        System.out.println(dao.getAllFamilies());
        return true;
    }

    public int count() {
        int number = dao.getAllFamilies().size();
        System.out.println("families overall: " + number);
        return number;
    }

    public Family getFamilyById(int id) {
        Family family = dao.getAllFamilies().get(id);
        System.out.println("family by id " + id + family);
        return family;
    }

    public List<Pet> getPets_(int id) {
        List<Pet> pets = dao.getAllFamilies().get(id).getPets();
        System.out.println("by this id, family's pets are: " + pets);
        return pets;
    }

    public boolean addPet(int id, Pet pet) {
        Family fam = dao.getAllFamilies().get(id);
        System.out.println("adding new pets: " + pet.getNickname());
        fam.getPets().add(pet);
        dao.saveFamily(fam);
        return true;
    }

}
