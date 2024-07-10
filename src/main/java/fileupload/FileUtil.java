package fileupload;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;

// 파일 업로드와 관련된 기능을 메서드로 정의한 유틸리티 클래스
public class FileUtil {

	// 파일 업로드 처리(매개변수 : request 내장객체와 디렉토리 경로)
	public static String uploadFile(HttpServletRequest req,
			String sDirectory)
			throws ServletException, IOException {
		
		/*
		 파일 첨부를 위한 <input> 태그의 name 속성값을 이용해서 Part 인스턴스를
		 생성한다. 이를 통해 파일을 서버에 저장할 수 있다.
		 */
		Part part = req.getPart("ofile");
		
		/*
		 Part 인스턴스에서 헤더값을 통해 파일의 원본 파일명을 얻어온다.
		 차후 콘솔을 통해 확인한다. 
		 */
		String partHeader = part.getHeader("content-disposition");
		System.out.println("partHeader=" + partHeader);
		
		/*
		 헤더값을 통해 얻어온 무자열에서 'filename= '을 구분자로 split 한다.
		 그러면 String 타입의 배열로 반환된다.
		 */
		String[] phArr = partHeader.split("filename=");
		/*
		 위 결과중 1번 인덱스의 값이 파일명이 된다. 여기서 double quotation을
		 제거해야 하므로 아래와 같이 replace()를 통해 제거해 준다. 단 double quotation
		 앞에 escape sequence 기호를 반드시 추가해야 한다.
		 */
		String originalFileName = phArr[1].trim().replace("\"", "");
		
		/*
		 전송된 파일이 있는 경우라면 서버의 디렉토리에 파일을 저장한다.
		 File.separator : OS마다 경로를 표시하는 기호가 다르므로
		 	해당 OS에 맞는 구분기호를 자동으로 추가해준다.
		 ※ separator는 이탤릭체 하늘색으로 표시되는데 이는 static 의미
		 */
		if (!originalFileName.isEmpty()) {
			part.write(sDirectory + File.separator + originalFileName);
		}
		
		// 원본 파일명을 반환해준다.
		return originalFileName;
	}
	
	/*
	 서버에 저장된 파일명을 변경한다.
	 파일명이 한글인 경우 웹브라우저에서 깨짐 현상이 발생될 수 있으므로
	 영문 혹은 숫자의 조합으로 변경하는 것이 안전하다.
	 */
	public static String renameFile(String sDirectory, String fileName) {
		/*
		 ext는 extension(확장자)을 의미하고 파일명에 '.'이 여러개일 수 있기
		 때문에 lastIndexOf() 메서드를 이용하여 확장자명을 잘라낸다.
		 */
		String ext = fileName.substring(fileName.lastIndexOf("."));
		/*
		 날짜와 시간을 이용해서 파일명으로 사용할 문자열을 생성한다.
		 '년월일_시분초밀리초'와 같은 형태가 된다.
		 */
		String now = new SimpleDateFormat("yyyyMMdd_HmsS")
				.format(new Date());
		// 파일명과 확장자명을 결합한다.
		String newFileName = now + ext;
		
		// 원본 파일명과 새로운 파일명을 이용해서 File 인스턴스를 섕성한다.
		File oldFile = new File(sDirectory + File.separator + fileName);
		File newFile = new File(sDirectory + File.separator + newFileName);
		// 파일명을 새로운 이름으로 변경한다.
		oldFile.renameTo(newFile);
		
		// 변경된 파일명을 반환한다.
		return newFileName;
	}
	
	// multiple 속성이 부여되어 2개 이상의 파일을 업로드
	public static ArrayList<String> multipleFile(HttpServletRequest req,
			String sDirectory)
			throws ServletException, IOException {
		
		// 파일명 저장을 위한 List 계열의 컬렉션 생성
		ArrayList<String> listFileName = new ArrayList<>();
		
		// getParts() 메서드로 Part 인스턴스를 컬렉션의 형태로 생성한다.
		Collection<Part> parts = req.getParts();
		for (Part part : parts) {
			
			// 전송된 폼값중에 파일이 아니라면 대상이 아니므로 반복문의 처음으로 이동한다.
			if (!part.getName().equals("ofile")) {
				continue;
			}
			
			// 폼값중 파일이 맞다면 헤더값을 얻어온다.
			String partHeader = part.getHeader("content-disposition");
			System.out.println("partHeader=" + partHeader);
			
			/*
			 헤더값을 통해 얻어온 무자열에서 'filename='을 구분자로 split 한다.
			 그러면 String 타입의 배열로 반환된다.
			 */
			String[] phArr = partHeader.split("filename=");
			/*
			 위 결과중 1번 인덱스의 값이 파일명이 된다. 여기서 double quotation을
			 제거해야 하므로 아래와 같이 replace()를 통해 제거해 준다. 단 double quotation
			 앞에 escape sequence 기호를 반드시 추가해야 한다.
			 */
			String originalFileName = phArr[1].trim().replace("\"", "");
			
			/*
			 전송된 파일이 있는 경우라면 서버의 디렉토리에 파일을 저장한다.
			 File.separator : OS마다 경로를 표시하는 기호가 다르므로
			 	해당 OS에 맞는 구분기호를 자동으로 추가해준다.
			 ※ separator는 이탤릭체 하늘색으로 표시되는데 이는 static 의미
			 */
			if (!originalFileName.isEmpty()) {
				part.write(sDirectory + File.separator + originalFileName);
			}
			
			// List에 파일명을 추가한다.
			listFileName.add(originalFileName);		
		}
		
		// 모든 파일의 업로드가 완료되면 파일명을 저장한 List를 반환한다.
		return listFileName;
	}
}
