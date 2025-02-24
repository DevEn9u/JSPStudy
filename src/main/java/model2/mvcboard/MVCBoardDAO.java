package model2.mvcboard;


import java.util.List;
import java.util.Map;
import java.util.Vector;

import common.DBConnPool;

// 커넥션 풀을 이용한 DB 연결을 위해 클래스 상속
public class MVCBoardDAO extends DBConnPool {
	
	// 없어도 되는 default constructor. 작성하지 않아도 자동으로 생성된다.
	public MVCBoardDAO() {
		super();
	}

	// 게시물의 개수를 카운트. 검색어가 있는 경우 where절을 동적으로 추가한다.
	public int selectCount(Map<String, Object> map) {
		int totalCount = 0;		
		String query = "SELECT COUNT(*) FROM mvcboard";
		if (map.get("searchWord") != null) {
			query += " WHERE " + map.get("searchField") + " "
					+ " LIKE '%" + map.get("searchWord") + "%'";
		}
		try {
			// 인파라미터가 없는 정적쿼리문을 실행하므로 Statement 인스턴스를 생성
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);
			rs.next();
			totalCount = rs.getInt(1);
		}
		catch (Exception e) {
			System.out.println("게시물 카운트 중 예외 발생");
			e.printStackTrace();
		}
		
		return totalCount;
	}
	
	// 목록에 출력할 게시물을 페이지 단위로 얻어오기 위한메서드
	public List<MVCBoardDTO> selectListPage(Map<String, Object> map) {
		
		// mvcboard 테이블을 대상으로 하므로 타입매개변수 확인 필요함
		List<MVCBoardDTO> board = new Vector<MVCBoardDTO>();
		
		/*
		페이징 처리를 위한 서브쿼리문 작성. 게시물의 순차적인 일련번호를 부여하는
		rownum을 통해 게시물을 구간에 맞게 인출한다.
		 */
		String query = "SELECT * FROM ( "
					 + " 	SELECT Tb.*, ROWNUM rNum FROM ( "
					 + " 		SELECT * FROM mvcboard ";
		if (map.get("searchWord") != null) {
			query += " WHERE " + map.get("searchField")
					+ " LIKE '%" + map.get("searchWord") + "%' ";
		}
		query += " 			ORDER BY idx DESC "
			   + "		) Tb "
			   + " ) "
			   + " WHERE rNum BETWEEN ? AND ?";
		
		try {
			// 인파라미터가 있는 동적 쿼리문
			psmt = con.prepareStatement(query);
			psmt.setString(1, map.get("start").toString());
			psmt.setString(2, map.get("end").toString());
			rs = psmt.executeQuery();

			while (rs.next()) {

				MVCBoardDTO dto = new MVCBoardDTO();
				
				dto.setIdx(rs.getString(1));
				dto.setName(rs.getString(2));
				dto.setTitle(rs.getString(3));
				dto.setContent(rs.getString(4));
				dto.setPostDate(rs.getDate(5)); // 날짜이므로 getDate() 사용
				dto.setOfile(rs.getString(6));
				dto.setSfile(rs.getString(7));
				dto.setDowncount(rs.getInt(8)); // 숫자이므로 getInt() 사용
				dto.setPass(rs.getString(9));
				dto.setVisitcount(rs.getInt(10)); // 숫자이므로 getInt() 사용
			
				// List에 추가한다.
				board.add(dto);
			}
		} 
		catch (Exception e) {
			System.out.println("게시물 조회 중 예외 발생");
			e.printStackTrace();
		}
		
		// 인출한 레코드를 저장한 List를 JSP로 반환한다.
		return board;
	}
	
	// 글쓰기 페이지에서 전송한 폼값을 테이블에 insert
	public int insertWrite(MVCBoardDTO dto) {
		int result = 0;
		/*
		 쿼리문에서 사용한 시퀀스는 모델1 게시판에서 생성한 것을 그대로 사용한다.
		 나머지 값들은 controller에서 받은 후 모델(DAO)로 전달한다.
		 */
		try {
			// 인파라미터가 있는 insert 쿼리문 작성
			String query = "INSERT INTO mvcboard ( "
					+ " idx, name, title, content, ofile, sfile, pass) "
					+ " VALUES ( "
					+ " seq_board_num.NEXTVAL, ?, ?, ?, ?, ?, ?)";
			// 일련번호의 경우 시퀀스를 통해 입력한다.
			
			// prepared 인스턴스 생성 및 인파라미터 설정
			psmt = con.prepareStatement(query);
			psmt.setString(1, dto.getName());
			psmt.setString(2, dto.getTitle());
			psmt.setString(3, dto.getContent());
			psmt.setString(4, dto.getOfile()); // 원본 파일명
			psmt.setString(5, dto.getSfile()); // 서버에 저장된 파일명
			psmt.setString(6, dto.getPass());
			
			// 쿼리문 실행
			result = psmt.executeUpdate();
			
		} catch (Exception e) {
			System.out.println("게시물 입력 중 예외 발생");
			e.printStackTrace();
		}
		
		return result;
	}
	
	// 상세보기를 위해 일련번호에 해당하는 레코드 1개를 인출해서 반환
	public MVCBoardDTO selectView(String idx) {
		// 하나의 레코드를 저장하기 위한 DTO 인스턴스 생성
		MVCBoardDTO dto = new MVCBoardDTO();
		// 인파라미터가 있는 SELECT 쿼리문
		String query = "SELECT * FROM mvcboard WHERE idx = ?";
		
		try {
			// 쿼리문의 인파라미터를 설정한 후 쿼리문 실행
			psmt = con.prepareStatement(query);
			psmt.setString(1, idx);
			rs = psmt.executeQuery();
			/*
			 일련번호는 중복되지 않으므로 단 한개의 게시물만 인출한다.
			 따라서 while문이 아닌 if문으로 처리한다. next()는 ResultSet으로
			 반환된 레코드를 확인해서 존재유무에 따라 true / false를 반환한다.
			 */
			// 결과를 DTO에 저장
			if(rs.next()) {
				dto.setIdx(rs.getString(1));
				dto.setName(rs.getString(2));
				dto.setTitle(rs.getString(3));
				dto.setContent(rs.getString(4));
				dto.setPostDate(rs.getDate(5));
				dto.setOfile(rs.getString(6));
				dto.setSfile(rs.getString(7));
				dto.setDowncount(rs.getInt(8));
				dto.setPass(rs.getString(9));
				dto.setVisitcount(rs.getInt(10));
			}
		} catch (Exception e) {
			System.out.println("게시물 상세보기 중 예외 발생");
			e.printStackTrace();
		}
		return dto;
	}
	
	// 게시물의 조회수를 증가
	public void updateVisitcount(String idx) {
		/* 게시물의 일련번호를 통해 visitcount를 1 증가시킨다.
		 이 컬럼은 number 타입이므로 사칙연산이 가능하다. */
		String query = "UPDATE mvcboard SET "
					+ " visitcount = visitcount + 1 "
					+ " WHERE idx = ?";
		
		try {
			psmt = con.prepareStatement(query);
			psmt.setString(1, idx);
			psmt.executeQuery();
		} catch (Exception e) {
			System.out.println("게시물 조회수 증가 중 예외 발생");
			e.printStackTrace();
		}
	}
	
	// 파일 다운로드시 카운트 증가
	public void downCountPlus(String idx) {
		String sql = "UPDATE mvcboard SET "
					+ " downcount = downcount + 1 "
					+ " WHERE idx = ? ";
		try {
			psmt = con.prepareStatement(sql);
			psmt.setString(1, idx);
			psmt.executeUpdate();
		} catch (Exception e) {}
	}
	
	// 패스워드 검증
	public boolean confirmPassword(String pass, String idx) {
		boolean isCorr = true;
		try {
			/*
			 패스워드와 일련번호 두가지 조건에 만족하는 게시물이 있는지 확인.
			 게시물을 인출할 목적이 아니므로 count() 함수면 충분하다.
			 */
			String sql = "SELECT COUNT(*) FROM mvcboard WHERE pass = ? AND idx = ?";
			psmt = con.prepareStatement(sql);
			psmt.setString(1, pass);
			psmt.setString(2, idx);
			rs = psmt.executeQuery();
			// count() 함수는 항상 결과가 있으므로 조건문 없이 호출한다.
			rs.next();
			if (rs.getInt(1) == 0) {
				// 조건에 맞는 게시물이 없다면 false로 변경
				isCorr = false;
			}
		} catch (Exception e) {
			// 예외가 발생하여 확인이 불가한 경우예도 false를 반환해야 한다.
			isCorr = false;
			e.printStackTrace();
		}
		return isCorr;
	}
	
	// 일련번호에 해당하는 게시물을 삭제 
	public int deletePost(String idx) {
		int result = 0;
		try {
			String query = "DELETE FROM mvcboard WHERE idx = ?";
			psmt = con.prepareStatement(query);
			psmt.setString(1, idx);
			result = psmt.executeUpdate();
		} catch (Exception e) {
			System.out.println("게시물 삭제 중 예외 발생");
			e.printStackTrace();
		}
		return result;
	}
	
	// 게시물 수정
	public int updatePost(MVCBoardDTO dto) {
		int result = 0;
		try {
			// 수정을 위한 update 쿼리문 작성(일련번호와 패스워드까지 조건문에 추가됨)
			String query = "UPDATE mvcboard"
						 + " SET title = ?, name = ?, content = ?, ofile = ?, sfile = ? "
						 + " WHERE idx = ? AND pass = ?";
			
			psmt = con.prepareStatement(query);
			psmt.setString(1, dto.getTitle());
			psmt.setString(2, dto.getName());
			psmt.setString(3, dto.getContent());
			psmt.setString(4, dto.getOfile());
			psmt.setString(5, dto.getSfile());
			psmt.setString(6, dto.getIdx());
			psmt.setString(7, dto.getPass());
			
			result = psmt.executeUpdate();
		}
		catch (Exception e) {
			System.out.println("게시물 수정 중 예외 발생");
			e.printStackTrace();
		}
		return result;
	}
	
}
