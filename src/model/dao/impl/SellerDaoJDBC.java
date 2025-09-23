package model.dao.impl;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class SellerDaoJDBC implements SellerDao {

    private java.sql.Connection conn;

    public SellerDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Seller obj) {

    }

    @Override
    public void update(Seller obj) {

    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public Seller findById(Integer id) {

        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            // Comando SQL de Consulta
            st = conn.prepareStatement(
                    "SELECT seller.*,department.Name as DepName "
                            + "FROM seller INNER JOIN department "
                            + "ON seller.DepartmentId = department.Id "
                            + "WHERE seller.Id = ?");

            st.setInt(1, id); // Define um id recebido por referência do méthod para buscar a tabela no banco de dados
            rs = st.executeQuery(); // Executa o comando e retorna o resultado em formato de tabela

            if (rs.next()) { // Testa se a tabela tem alguma coluna diferente de zero

                Department dep = new Department(); // Cria instância do Department
                // Atualizam os objetos do Department com os dados do Banco de Dados filtrado atráves do get com o nome da Tabela requerida
                dep.setId(rs.getInt("DepartmentId"));
                dep.setName(rs.getString("DepName"));

                Seller obj = new Seller(); // Cria instância do Seller
                // Atualizam os objetos do Seller com os dados do Banco de Dados filtrado atráves do get com o nome da Tabela requerida
                obj.setId(rs.getInt("Id"));
                obj.setName(rs.getString("Name"));
                obj.setEmail(rs.getString("Email"));
                obj.setBaseSalary(rs.getDouble("BaseSalary"));
                obj.setBirthDate(rs.getDate("BirthDate"));
                obj.setDepartment(dep); // Realiza a composição de Classes com os dados informados pelo Banco de Dados

                return obj; // Retorna o resultado
            }
            return null; // Retorna nulo se não houver coluna diferente de zero
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public List<Seller> findAll() {
        return List.of();
    }
}
