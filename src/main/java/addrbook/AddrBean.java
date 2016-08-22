package addrbook;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AddrBean {

	Connection conn = null;
	PreparedStatement pstmt = null;
	
	String jdbc_driver = "com.mysql.jdbc.Driver";
	String jdbc_url = "jdbc:mysql://127.0.0.1:3306/hanbit1.addrbook";
	
	//DB연결 메서드
	void connect(){
		try{
			Class.forName(jdbc_driver);
			conn=DriverManager.getConnection(jdbc_url, "developer", "developer");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	void disconnect(){
		if(pstmt != null){
			try{
				pstmt.close();
			}catch(SQLException e){
				e.printStackTrace();
			}
		}//end if(pstmt != null)
		
		if(conn != null){
			try{
				conn.close();
			}catch(SQLException e){
				e.printStackTrace();
			}
		}//end if(conn != null)
	}
	
	//수정된 주소록 내용 갱신을 위한 메서드
	public boolean updateDB(AddrBook addrbook){
		connect();
		
		String sql = "update addrbook set ab_name=?, ab_email=?, ab_birth=?, ab_tel=?, ab_comdept=?, ab_memo=? where ab_id=?";
		
		try{
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, addrbook.getAb_name());
			pstmt.setString(2, addrbook.getAb_email());
			pstmt.setString(3, addrbook.getAb_birth());
			pstmt.setString(4, addrbook.getAb_tel());
			pstmt.setString(5, addrbook.getAb_comdept());
			pstmt.setString(6, addrbook.getAb_memo());
			pstmt.setInt(7, addrbook.getAb_id());
			pstmt.executeUpdate();
		} catch(SQLException e){
			e.printStackTrace();
			return false;
		} finally{
			disconnect();
		}//end try
		
		return true;
	}//end updateDB(AddrBook addrbook)
	
	//특정 주소록 삭제 메서드
	public boolean deleteDB(int ab_id){
		connect();
		
		String sql = "delete from addrbook where ab_id=?";
		
		try{
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, ab_id);
			pstmt.executeUpdate();
		} catch(SQLException e){
			e.printStackTrace();
			return false;
		} finally{
			disconnect();
		}//end try
		
		return true;
	}//end deleteDB(int gb_id)
	
	//신규 주소록을 추가하는 메서드
	public boolean insertDB(AddrBook addrbook){
		connect();
		//ab_id는 DB에서 auto_increment이므로 입력하지 않는다.
		String sql = "insert into addrbook(ab_name, ab_email, ab_birth, ab_tel, ab_comdept, ab_memo)";
		
		try{
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, addrbook.getAb_name());
			pstmt.setString(2, addrbook.getAb_email());
			pstmt.setString(3, addrbook.getAb_birth());
			pstmt.setString(4, addrbook.getAb_tel());
			pstmt.setString(5, addrbook.getAb_comdept());
			pstmt.setString(6, addrbook.getAb_memo());
			pstmt.executeUpdate();
		} catch(SQLException e){
			e.printStackTrace();
			return false;
		} finally{
			disconnect();
		}//end try
		
		return true;
	}//end insertDB(AddrBook addrbook)
	
	//특정 주소록을 가져오는 메서드
	public AddrBook getDB(int ab_id){
		connect();
		
		String sql = "select * from addrbook where ab_id=?";
		AddrBook addrbook = new AddrBook();
		
		try{
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, ab_id);
			ResultSet rs = pstmt.executeQuery();
			
			//데이터가 하나만 있으므로 rs.next()를 한번만 실행
			rs.next();
			addrbook.setAb_id(rs.getInt("ab_id"));
			addrbook.setAb_name(rs.getString("ab_name"));
			addrbook.setAb_email(rs.getString("ab_email"));
			addrbook.setAb_birth(rs.getString("ab_birth"));
			addrbook.setAb_tel(rs.getString("ab_tel"));
			addrbook.setAb_comdept(rs.getString("ab_comdept"));
			addrbook.setAb_memo(rs.getString("ab_memo"));
			rs.close();
		} catch(SQLException e){
			e.printStackTrace();
		} finally{
			disconnect();
		}//end try
		
		return addrbook;
	}//end getDB(int ab_id)
	
	//전체 주소록을 가져오는 메서드
	public ArrayList<AddrBook> getDBList(){
		connect();
		
		ArrayList<AddrBook> datas = new ArrayList<>();
		
		String sql = "select * from addrbook order by ab_id dessc";
		
		try{
			pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()){
				AddrBook addrbook = new AddrBook();
				
				addrbook.setAb_id(rs.getInt("ab_id"));
				addrbook.setAb_name(rs.getString("ab_name"));
				addrbook.setAb_email(rs.getString("ab_email"));
				addrbook.setAb_birth(rs.getString("ab_birth"));
				addrbook.setAb_tel(rs.getString("ab_tel"));
				addrbook.setAb_comdept(rs.getString("ab_comdept"));
				addrbook.setAb_memo(rs.getString("ab_memo"));
				
				datas.add(addrbook);
			}//end while(rs.next())
			rs.close();
		} catch(SQLException e){
			e.printStackTrace();
		} finally{
			disconnect();
		}//end try
		
		return datas;
	}//end getDBList()
}
