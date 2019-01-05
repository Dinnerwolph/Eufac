package net.euphalys.eufac.database;

import net.euphalys.eufac.EuFac;
import net.euphalys.eufac.group.Group;
import org.bukkit.entity.Player;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
}
