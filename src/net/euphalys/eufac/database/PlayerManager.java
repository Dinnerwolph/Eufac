package net.euphalys.eufac.database;

import net.euphalys.eufac.EuFac;
import net.euphalys.eufac.group.Group;
import org.bukkit.entity.Player;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Dinnerwolph
 */
public class PlayerManager {

    private final DataSource dataSource;

    public PlayerManager() {
        dataSource = EuFac.getInstance().databaseManager.getDataSource();
    }

    public boolean createUser(Player player) {
        if (exist(player.getUniqueId()))
            return true;

        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement("INSERT INTO `user`(`uuid`, `playerName`) VALUES (?,?)");
            statement.setString(1, player.getUniqueId().toString());
            statement.setString(2, player.getName());
            statement.executeUpdate();
            statement.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean exist(UUID uuid) {
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT `id` FROM `user` WHERE `uuid`=?");
            statement.setString(1, uuid.toString());
            ResultSet set = statement.executeQuery();
            if (set.next()) {
                statement.close();
                return true;
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void unload(int boostXp, int id) {
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement("UPDATE `user` SET `boostXp`=? WHERE `id`=?");
            statement.setInt(1, boostXp);
            statement.setInt(2, id);
            statement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getEuphId(UUID uuid) {
        int returnint = 0;
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT `id` FROM user WHERE `uuid`=?");
            statement.setString(1, uuid.toString());
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            returnint = resultSet.getInt("id");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return returnint;
    }

    public Group getGroup(UUID uuid) {
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT `groupId` FROM `user` WHERE `uuid`=?");
            statement.setString(1, uuid.toString());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int groupid = resultSet.getInt("groupId");
                connection.close();
                Group group = EuFac.getInstance().groupMap.get(groupid);
                if (group == null) {
                    connection = dataSource.getConnection();
                    statement = connection.prepareStatement("SELECT * FROM `group` WHERE `id`=?");
                    statement.setInt(1, groupid);
                    resultSet = statement.executeQuery();
                    resultSet.next();
                    group = new Group(resultSet.getInt("id"), resultSet.getString("name"),
                            resultSet.getString("prefix"), resultSet.getString("suffix"),
                            resultSet.getInt("ladder"));
                    EuFac.getInstance().groupMap.put(groupid, group);
                }
                connection.close();
                return group;
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Group updateGroup(int id) {
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM `group` WHERE `id`=?");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            Group group = new Group(resultSet.getInt("id"), resultSet.getString("name"),
                    resultSet.getString("prefix"), resultSet.getString("suffix"),
                    resultSet.getInt("ladder"));
            connection.close();
            return group;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setGroup(String name, int groupId) {
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement("UPDATE `user` SET `groupId`=? WHERE `playerName`=?");
            statement.setString(2, name);
            statement.setInt(1, groupId);
            statement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void useXPBOOST(int id) {
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement("INSERT INTO `boostxp`(`euphid`, `dateuse`) VALUES (?, NOW())");
            statement.setInt(1, id);
            statement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public List<LocalDateTime> getXPBOOSTUse(int id) {
        List<LocalDateTime> dateList = new ArrayList<>();
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT `dateuse` FROM boostxp WHERE euphid=? ORDER BY `boostxp`.`id` DESC");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next())
                dateList.add(LocalDateTime.parse(resultSet.getString("dateuse").replace(" ", "T")));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dateList;
    }

    public Integer getXpBoost(int id) {
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT `boostXp` FROM `user` WHERE `id`=?");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            int i = resultSet.getInt("boostXp");
            connection.close();
            return i;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
