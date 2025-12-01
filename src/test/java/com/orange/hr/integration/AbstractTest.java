package com.orange.hr.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.datatype.DataType;
import org.dbunit.dataset.datatype.DataTypeException;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.h2.H2DataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.Types;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionDbUnitTestExecutionListener.class
})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AbstractTest {
    private static IDatabaseConnection dbUnitConnection;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    void setUp() throws Exception {
        if (dbUnitConnection == null) {
            dbUnitConnection = new DatabaseConnection(dataSource.getConnection());

            DatabaseConfig config = dbUnitConnection.getConfig();

            config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new CustomH2DataTypeFactory());
            config.setProperty(DatabaseConfig.FEATURE_CASE_SENSITIVE_TABLE_NAMES, false);
            config.setProperty(DatabaseConfig.FEATURE_ALLOW_EMPTY_FIELDS, true);
        }

    }

    @BeforeEach
    void setupBeforeEach() {
        objectMapper.clearCaches();
    }

    @BeforeEach
    void setUpBeforeEach() {
        objectMapper.clearCaches();
    }

    public void prepareDB(String path) throws DatabaseUnitException, SQLException {

        FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
        IDataSet dataSet = builder.build(getClass().getResourceAsStream(path));
        dbUnitConnection.getConnection().createStatement().execute("SET REFERENTIAL_INTEGRITY FALSE");
        DatabaseOperation.CLEAN_INSERT.execute(dbUnitConnection, dataSet);
        dbUnitConnection.getConnection().createStatement().execute("SET REFERENTIAL_INTEGRITY TRUE");
    }

    public static class CustomH2DataTypeFactory extends H2DataTypeFactory {
        @Override
        public DataType createDataType(int sqlType, String sqlTypeName) throws DataTypeException {
            if (sqlType == Types.OTHER && sqlTypeName.startsWith("ENUM")) {
                return DataType.VARCHAR;
            }
            return super.createDataType(sqlType, sqlTypeName);
        }
    }
}
