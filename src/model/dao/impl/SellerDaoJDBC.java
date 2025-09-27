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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    /**
     * Consulta a tabela seller por Id
     *
     * @param id fornecido pelo main e utilizado na consulta SQL
     * @return obj retorna o resultado da consulta
     * @throws  SQLException caso ocorra erro ao acessar o banco de dados
     */
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
                Department dep = instantiateDepartment(rs); // Envia o resultset como parâmetro e recebe o Department atualizado pelo method
                Seller obj = instantiateSeller(rs, dep); // Envia o result set e o department(composição) como parâmetro e recebe o obj atualizado pelo method
                return obj; // Retorna o resultado da consulta
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

    /**
     * Reutiliza a instanciação para a atualização dos  Atributos de Department pelo Banco de Dados
     *
     * @param rs parâmetro do method findbyId
     * @return dep atualizado com os dados do banco de bados
     * @throws SQLException dispersa exceção caso ocorra algum erro ao acessar o banco de dados
     */
    private Department instantiateDepartment(ResultSet rs) throws SQLException{
        Department dep = new Department(); // Cria instância do Department
        // Atualizam os objetos do Department com os dados do Banco de Dados filtrado atráves do get com o nome da Tabela requerida
        dep.setId(rs.getInt("DepartmentId"));
        dep.setName(rs.getString("DepName"));
        return dep;
    }

    /**
     * Reutiliza a instanciação para a atualização dos Atributos do Seller pelo Banco de Dados
     *
     * @param rs parâmetro do method findById
     * @param dep parâmetro do method findById
     * @return obj atualizado com os dados do banco de dados e objetos da classe Department
     * @throws SQLException dispersa exceção caso ocorra algum erro ao acessar o banco de dados
     */
    private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException{
        Seller obj = new Seller(); // Cria instância do Seller
        // Atualizam os objetos do Seller com os dados do Banco de Dados filtrado atráves do get com o nome da Tabela requerida
        obj.setId(rs.getInt("Id"));
        obj.setName(rs.getString("Name"));
        obj.setEmail(rs.getString("Email"));
        obj.setBaseSalary(rs.getDouble("BaseSalary"));
        obj.setBirthDate(rs.getDate("BirthDate"));
        obj.setDepartment(dep); // Realiza a composição de Classes com os dados informados pelo Banco de Dados
        return obj;
    }

    /**
     * Consulta a tabela seller
     *
     * @return list com os dados
     */
    @Override
    public List<Seller> findAll() {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            // Comando SQL de Consulta
            st = conn.prepareStatement(
                    "SELECT seller.*,department.Name as DepName "
                            + "FROM seller INNER JOIN department "
                            + "ON seller.DepartmentId = department.Id "
                            + "ORDER BY Name");
            rs = st.executeQuery(); // Executa o comando e retorna o resultado em formato de tabela

            List<Seller> list = new ArrayList<>();
            Map<Integer, Department> map = new HashMap<>(); // Armazenará instâncias

            // Percorre a tabela enquanto existir uma próxima coluna
            while (rs.next()) {
                Department dep = map.get(rs.getInt("DepartmentId")); // Verifica se um DepartmentId existe dentro do Map, se não, retorna nulo

                if (dep == null) {
                    dep = instantiateDepartment(rs); // Envia o resultset como parâmetro e recebe o Department atualizado pelo method
                    map.put(rs.getInt("DepartmentId"), dep); // Armazena a chave do deparmentId da coluna atual e o Department relacionado
                }

                Seller obj = instantiateSeller(rs, dep); // Envia o result set e o department(composição) como parâmetro e recebe o obj atualizado pelo method
                list.add(obj); // Armazena todas as colunas em uma lista
            }
            return list; // Retorna a lista com os dados
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    /**
     * Consulta a tabela seller por DepartmentId
     *
     * @param department para conseguir o id da Classe
     * @return list com todos os dados requeridos
     */
    @Override
    public List<Seller> findByDepartment(Department department) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            // Comando SQL de Consulta
            st = conn.prepareStatement(
                    "SELECT seller.*,department.Name as DepName "
                    + "FROM seller INNER JOIN department "
                    + "ON seller.DepartmentId = department.Id "
                    + "WHERE DepartmentId = ? "
                            + "ORDER BY Name");
            st.setInt(1, department.getId()); // Define um id recebido por referência do méthod para buscar a tabela no banco de dados
            rs = st.executeQuery(); // Executa o comando e retorna o resultado em formato de tabela

            List<Seller> list = new ArrayList<>();
            Map<Integer, Department> map = new HashMap<>(); // Armazenará instâncias

            // Percorre a tabela enquanto existir uma próxima coluna
            while (rs.next()) {
                Department dep = map.get(rs.getInt("DepartmentId")); // Verifica se um DepartmentId existe dentro do Map, se não, retorna nulo

                if (dep == null) {
                    dep = instantiateDepartment(rs); // Envia o resultset como parâmetro e recebe o Department atualizado pelo method
                    map.put(rs.getInt("DepartmentId"), dep); // Armazena a chave do deparmentId da coluna atual e o Department relacionado
                }

                Seller obj = instantiateSeller(rs, dep); // Envia o result set e o department(composição) como parâmetro e recebe o obj atualizado pelo method
                list.add(obj); // Armazena todas as colunas em uma lista
            }
            return list; // Retorna a lista com os dados
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }
}
