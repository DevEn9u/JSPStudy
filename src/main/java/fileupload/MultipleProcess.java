package fileupload;

import java.io.IOException;
import java.util.ArrayList;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/*
 어노테이션을 통해 매핑한다 .파일 업로드 폼에서 submit하면
 요청을 받아 처리한다.
 */
@WebServlet("/13FileUpload/MultipleProcess.do")
/*
 maxFileSize : 개별 파일의 최대용량을 1Mb로 설정
 maxRequestSize : 첨부한 전체 파일의 용량을 10Mb로 설정
 */
@MultipartConfig(
      maxFileSize = 1024 * 1024 * 1,
      maxRequestSize = 1024 * 1024 * 10
)
public class MultipleProcess extends HttpServlet {

   private static final long serialVersionUID = 1L;
   
   @Override
   protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
         throws ServletException, IOException {
      try {
         // 업로드 디렉토리의 물리적 경로
         String saveDirectory = getServletContext().getRealPath("/Uploads");
         
         // 선택한 파일 업로드
         ArrayList<String> listFileName = FileUtil.multipleFile(req, saveDirectory);
         
         // List에 저장된 파일의 개수만큼 반복하여 파일명 변경
         for(String originalFileName : listFileName) {
            String savedFileName = FileUtil.renameFile(saveDirectory, originalFileName);
         }
         // 업로드가 완료되면 파일목록으로 이동한다.
         resp.sendRedirect("FileList.jsp");
         
      } catch (Exception e) {
         e.printStackTrace();
         req.setAttribute("errorMessage", "파일 업로드 오류");
         req.getRequestDispatcher("MultipleUploadMain.jsp").forward(req, resp);
      }
   }
}
