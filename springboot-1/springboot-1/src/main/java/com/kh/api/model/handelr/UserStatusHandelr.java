package com.kh.api.model.handelr;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import com.kh.api.model.vo.UserStatus;

@MappedTypes(UserStatus.class)
@MappedJdbcTypes(JdbcType.INTEGER)
public class UserStatusHandelr extends BaseTypeHandler<UserStatus> {

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, UserStatus parameter, JdbcType jdbcType)
			throws SQLException {
		ps.setInt(i, Integer.parseInt(parameter.getUserStatus()));
	}

	@Override
	public UserStatus getNullableResult(ResultSet rs, String columnName) throws SQLException {
		return UserStatus.getValueOfUserStatus(rs.getInt(columnName));
				
	}

	@Override
	public UserStatus getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		return UserStatus.getValueOfUserStatus(rs.getInt(columnIndex));
	}

	@Override
	public UserStatus getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		return UserStatus.getValueOfUserStatus(cs.getInt(columnIndex));
	}
	
}
