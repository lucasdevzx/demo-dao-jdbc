package application;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

import java.util.List;
import java.util.Scanner;

public class Program2 {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("=== TEST 1: department findById ===");
        DepartmentDao departmentDao = DaoFactory.createDepartmentDao();
        Department department = departmentDao.findById(1);
        System.out.println(department);

        System.out.println("=== TEST 2: department findAll ===");
        List<Department> list = departmentDao.findAll();
        for (Department obj : list) {
            System.out.println(obj);
        }

        System.out.println("=== TEST 3: department insert ===");
        Department newDepartment = new Department(null, "Computaria");
        departmentDao.insert(newDepartment);
        System.out.println("Insert completed! Id number = " + department.getId());

        System.out.println("=== TEST 4: department update ===");
        //department = departmentDao.findById(5);
        department.setId(5);
        department.setName("Garoto de Programa");
        departmentDao.update(department);
        System.out.println("Update completed!");

        System.out.println("=== TEST 5: department delete ===");
        System.out.print("Select id for delete department: ");
        int id = sc.nextInt();
        departmentDao.deleteById(id);
        System.out.println("Delete completed!");

        sc.close();
    }
}
