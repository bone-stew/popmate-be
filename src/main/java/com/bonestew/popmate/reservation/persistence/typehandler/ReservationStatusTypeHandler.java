package com.bonestew.popmate.reservation.persistence.typehandler;

import com.bonestew.popmate.reservation.domain.ReservationStatus;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

@MappedJdbcTypes(JdbcType.INTEGER)
@MappedTypes(ReservationStatus.class)
public class ReservationStatusTypeHandler extends BaseTypeHandler<ReservationStatus> {

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, ReservationStatus status, JdbcType jdbcType) throws SQLException {
        preparedStatement.setInt(i, status.getCode());
    }

    @Override
    public ReservationStatus getNullableResult(ResultSet resultSet, String s) throws SQLException {
        int code = resultSet.getInt(s);
        return ReservationStatus.fromCode(code);
    }

    @Override
    public ReservationStatus getNullableResult(ResultSet resultSet, int i) throws SQLException {
        int code = resultSet.getInt(i);
        return ReservationStatus.fromCode(code);
    }

    @Override
    public ReservationStatus getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        int code = callableStatement.getInt(i);
        return ReservationStatus.fromCode(code);
    }
}
