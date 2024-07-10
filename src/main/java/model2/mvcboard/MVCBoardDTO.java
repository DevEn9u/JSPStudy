package model2.mvcboard;

// mvcboard 테이블의 레코드를 저장하기 위한 DTO 클래스
public class MVCBoardDTO {

	private String idx;
	private String name;
	private String title;
	private String content;
	private java.sql.Date postDate; // 날짜타입이므로 Date형
	private String ofile;
	private String sfile;
	private int downcount; // 다운로드 카운트 Int형
	private String pass;
	private int visitcount; // 게시물 조회수 카운트 Int형
	
	// 생성자는 정의하지 않아도 됨.
	
	// Getter / Setter
	public String getIdx() {
		return idx;
	}
	public void setIdx(String idx) {
		this.idx = idx;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public java.sql.Date getPostDate() {
		return postDate;
	}
	public void setPostDate(java.sql.Date postDate) {
		this.postDate = postDate;
	}
	public String getOfile() {
		return ofile;
	}
	public void setOfile(String ofile) {
		this.ofile = ofile;
	}
	public String getSfile() {
		return sfile;
	}
	public void setSfile(String sfile) {
		this.sfile = sfile;
	}
	public int getDowncount() {
		return downcount;
	}
	public void setDowncount(int downcount) {
		this.downcount = downcount;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	public int getVisitcount() {
		return visitcount;
	}
	public void setVisitcount(int visitcount) {
		this.visitcount = visitcount;
	}
}
