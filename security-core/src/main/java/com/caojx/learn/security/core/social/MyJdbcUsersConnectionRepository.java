package com.caojx.learn.security.core.social;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.social.connect.*;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 重写 JdbcUsersConnectionRepository，因为MySql 8.0.19中不支持 UserConnection 表中的rank字段，我把UserConnection表字段修改为rank_1
 *
 * @author caojx
 * @version $Id: MyJdbcUsersConnectionRepository.java,v 1.0 2020/3/9 7:24 下午 caojx
 * @date 2020/3/9 7:24 下午
 */
public class MyJdbcUsersConnectionRepository implements UsersConnectionRepository {

    private final JdbcTemplate jdbcTemplate;
    private final ConnectionFactoryLocator connectionFactoryLocator;
    private final TextEncryptor textEncryptor;
    private ConnectionSignUp connectionSignUp;
    private String tablePrefix = "";

    public MyJdbcUsersConnectionRepository(DataSource dataSource, ConnectionFactoryLocator connectionFactoryLocator, TextEncryptor textEncryptor) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.connectionFactoryLocator = connectionFactoryLocator;
        this.textEncryptor = textEncryptor;
    }

    public void setConnectionSignUp(ConnectionSignUp connectionSignUp) {
        this.connectionSignUp = connectionSignUp;
    }

    public void setTablePrefix(String tablePrefix) {
        this.tablePrefix = tablePrefix;
    }

    public List<String> findUserIdsWithConnection(Connection<?> connection) {
        ConnectionKey key = connection.getKey();
        List<String> localUserIds = this.jdbcTemplate.queryForList("select userId from " + this.tablePrefix + "UserConnection where providerId = ? and providerUserId = ?", String.class, new Object[]{key.getProviderId(), key.getProviderUserId()});
        if (localUserIds.size() == 0 && this.connectionSignUp != null) {
            String newUserId = this.connectionSignUp.execute(connection);
            if (newUserId != null) {
                this.createConnectionRepository(newUserId).addConnection(connection);
                return Arrays.asList(newUserId);
            }
        }

        return localUserIds;
    }

    public Set<String> findUserIdsConnectedTo(String providerId, Set<String> providerUserIds) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("providerId", providerId);
        parameters.addValue("providerUserIds", providerUserIds);
        final Set<String> localUserIds = new HashSet();
        return (Set) (new NamedParameterJdbcTemplate(this.jdbcTemplate)).query("select userId from " + this.tablePrefix + "UserConnection where providerId = :providerId and providerUserId in (:providerUserIds)", parameters, new ResultSetExtractor<Set<String>>() {
            public Set<String> extractData(ResultSet rs) throws SQLException, DataAccessException {
                while (rs.next()) {
                    localUserIds.add(rs.getString("userId"));
                }

                return localUserIds;
            }
        });
    }

    public ConnectionRepository createConnectionRepository(String userId) {
        if (userId == null) {
            throw new IllegalArgumentException("userId cannot be null");
        } else {
            return new MyJdbcConnectionRepository(userId, this.jdbcTemplate, this.connectionFactoryLocator, this.textEncryptor, this.tablePrefix);
        }
    }
}