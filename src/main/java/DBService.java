import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBService {
    private String url;
    private String user;
    private String password;
    private int num;
    private Connection conn;

    DBService(String url, String user, String password, int num) {
        this.url = url;
        this.user = user;
        this.password = password;
        this.num = num;
    }

    DBService() {
    }

    void setConnection() {
        try {
            Class.forName("org.h2.Driver");
            conn = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void closeConnection() {
        try {
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void fillInTable() {
        Statement st = null;
        try {
            st = conn.createStatement();
            st.execute("CREATE TABLE IF NOT EXISTS TEST(FIELD INT PRIMARY KEY)");
            st.execute("DELETE FROM TEST");
            st.close();
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO TEST VALUES (?)");
            for (int i = 1; i <= num; i++) {
                stmt.setInt(1, i);
                stmt.executeUpdate();
            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Entry getEntryFromRS(ResultSet rs) throws SQLException {
        Entry result = new Entry();
        result.setField(rs.getString("FIELD"));
        return result;
    }

    List<Entry> select() {
        List<Entry> result = new ArrayList<Entry>();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM TEST");
            while (rs.next()) {
                result.add(getEntryFromRS(rs));
            }
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
