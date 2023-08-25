package com.bonestew.popmate.reservation.persistence.typehandler;

import com.bonestew.popmate.reservation.domain.UserReservationStatus;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

@MappedJdbcTypes(JdbcType.INTEGER)
@MappedTypes(UserReservationStatus.class)
public class UserReservationStatusTypeHandler extends BaseTypeHandler<UserReservationStatus> {

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement,
                                    int i,
                                    UserReservationStatus status,
                                    JdbcType jdbcType) throws SQLException {
        preparedStatement.setInt(i, status.getCode());
    }

    @Override
    public UserReservationStatus getNullableResult(ResultSet resultSet, String s) throws SQLException {
        int code = resultSet.getInt(s);
        return UserReservationStatus.fromCode(code);
    }

    @Override
    public UserReservationStatus getNullableResult(ResultSet resultSet, int i) throws SQLException {
        int code = resultSet.getInt(i);
        return UserReservationStatus.fromCode(code);
    }

    @Override
    public UserReservationStatus getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        int code = callableStatement.getInt(i);
        return UserReservationStatus.fromCode(code);
    }
}
