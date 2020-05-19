package sec03.brd08;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;

/**
 * Servlet implementation class BoardController
 */
@WebServlet("/board/*")
public class BoardController extends HttpServlet {
	private static String ARTICLE_IMAGE_REPO = "D:\\board\\article_image";	//�겢�옒�뒪�뿉 泥⑤��븳 �씠誘몄� ���옣 �쐞移섎�� �긽�닔濡� �꽑�뼵
	BoardService boardService;
	ArticleVO articleVO;

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		boardService = new BoardService();	//�꽌釉붾┸ 珥덇린�솕 �떆 BoardService 媛앹껜 �깮�꽦
		articleVO = new ArticleVO();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException {
		doHandle(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doHandle(request, response);
	}

	private void doHandle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String nextPage = "";
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");
		HttpSession session;  //�떟湲��뿉 ���븳 遺�紐④�踰덊샇瑜� ���옣�븯湲� �쐞�빐 �꽭�뀡 �궗�슜
		String action = request.getPathInfo();	//�슂泥�紐� 媛��졇�샂
		System.out.println("action:" + action);
		try {
			List<ArticleVO> articlesList = new ArrayList<ArticleVO>();
			if (action==null){	//理쒖큹�슂泥��떆
				String _section=request.getParameter("section");
				String _pageNum=request.getParameter("pageNum");
				//section怨� pageNum�뾾�쑝硫� 媛� 1濡� 珥덇린�솕
				int section = Integer.parseInt(((_section==null)? "1":_section) );  
				int pageNum = Integer.parseInt(((_pageNum==null)? "1":_pageNum));
				//hashmap�뿉 ���옣�븳 �썑 硫붿냼�뱶濡� �꽆源�
				Map<String, Integer> pagingMap = new HashMap<String, Integer>();
				pagingMap.put("section", section);
				pagingMap.put("pageNum", pageNum);
				Map articlesMap=boardService.listArticles(pagingMap);
				articlesMap.put("section", section);
				articlesMap.put("pageNum", pageNum);
				request.setAttribute("articlesMap", articlesMap);
				nextPage = "/board07/ listArticles.jsp";
				}else if(action.equals("/listArticles.do")){  //action 媛믪씠 listArticles.do硫� �쟾泥� 湲� 議고쉶		
				//湲� 紐⑸줉�뿉�꽌 紐낆떆�쟻�쑝濡� �럹�씠吏� 踰덊샇瑜� �닃�윭�꽌 �슂泥��븳 寃쎌슦 section媛믨낵 pagenum 媛믪쓣 媛��졇�샂
				String _section=request.getParameter("section");
				String _pageNum=request.getParameter("pageNum");
				int section = Integer.parseInt(((_section==null)? "1":_section) );
				int pageNum = Integer.parseInt(((_pageNum==null)? "1":_pageNum));
				Map pagingMap=new HashMap();
				pagingMap.put("section", section);
				pagingMap.put("pageNum", pageNum);
				Map articlesMap=boardService.listArticles(pagingMap);
				articlesMap.put("section", section);
				articlesMap.put("pageNum", pageNum);
				request.setAttribute("articlesMap", articlesMap);
				nextPage = "/board07/listArticles.jsp";
			} else if (action.equals("/articleForm.do")) {	//articleForm.do濡� �슂泥��떆 湲��벐湲곗갹�씠 �굹���궓
				nextPage = "/board07/articleForm.jsp";
			} else if (action.equals("/addArticle.do")) {
				int articleNO = 0;
				Map<String, String> articleMap = upload(request, response);	//�뙆�씪�뾽濡쒕뱶湲곕뒫 �궗�슜�쓣 �쐞�빐 upload濡� �슂泥� �쟾�떖
				//articleMap�뿉 ���옣�맂 湲� �젙蹂대�� �떎�떆 媛��졇�샂
				String title = articleMap.get("title");		
				String content = articleMap.get("content");
				String imageFileName = articleMap.get("imageFileName");
				//湲��벐湲곗갹�뿉�꽌 �엯�젰�맂 �젙蹂대�� ArticleVO 媛앹껜�뿉 �꽕�젙�븳 �썑 addArticle()濡� �쟾�떖
				articleVO.setParentNO(0); //�깉 湲��쓽 遺�紐� 湲� 踰덊샇瑜� 0�쑝濡� �꽕�젙
				articleVO.setId("geonhun"); //怨꾩젙�젙蹂� �꽕�젙
				articleVO.setTitle(title);
				articleVO.setContent(content);
				articleVO.setImageFileName(imageFileName);
				articleNO = boardService.addArticle(articleVO); //�깉 湲��뿉 ���븳 湲� 踰덊샇 媛��졇�삤湲�
				if (imageFileName != null && imageFileName.length() != 0) { //�뙆�씪�쓣 泥⑤��븳 寃쎌슦�뿉留� �닔�뻾
					//temp �뤃�뜑�뿉 �엫�떆濡� �뾽濡쒕뱶 �맂 �뙆�씪 媛앹껜瑜� �깮�꽦
					File srcFile = new File(ARTICLE_IMAGE_REPO + "\\" + "temp" + "\\" + imageFileName);
					File destDir = new File(ARTICLE_IMAGE_REPO + "\\" + articleNO);
					destDir.mkdirs();//湲� 踰덊샇濡� �뤃�뜑 �깮�꽦
					//temp�뤃�뜑�쓽 �뙆�씪�쓣 湲� 踰덊샇瑜� �씠由꾩쑝濡� �븯�뒗 �뤃�뜑濡� �씠�룞
					FileUtils.moveFileToDirectory(srcFile, destDir, true);
				}
				PrintWriter pw = response.getWriter();
				pw.print("<script>" + "  alert('�깉 湲��쓣 異붽��뻽�뒿�땲�떎.');" + " location.href='" + request.getContextPath()
						+ "/board/listArticles.do';" + "</script>");

				return;
			} else if (action.equals("/viewArticle.do")) {
				String articleNO = request.getParameter("articleNO"); //�긽�꽭蹂닿린 �슂泥��떆 articleNo 媛� 媛��졇�샂
				articleVO = boardService.viewArticle(Integer.parseInt(articleNO));
				request.setAttribute("article", articleVO); //article�냽�꽦�쑝濡� 湲� �젙蹂대�� 諛붿씤�뵫
				nextPage = "/board07/viewArticle.jsp";
			} else if (action.equals("/modArticle.do")) { //�닔�젙�븯湲� �씤 寃쎌슦
				Map<String, String> articleMap = upload(request, response); //upload 硫붿냼�뱶瑜� �씠�슜, �닔�젙�맂 �뜲�씠�꽣瑜� map�뿉 ���옣�븯怨� 諛섑솚
				int articleNO = Integer.parseInt(articleMap.get("articleNO"));
				articleVO.setArticleNO(articleNO);
				String title = articleMap.get("title");
				String content = articleMap.get("content");
				String imageFileName = articleMap.get("imageFileName");
				articleVO.setParentNO(0);
				articleVO.setId("geonhun");
				articleVO.setTitle(title);
				articleVO.setContent(content);
				articleVO.setImageFileName(imageFileName);
				boardService.modArticle(articleVO); //�쟾�넚�맂 湲� �젙蹂대�� �씠�슜�빐 湲��쓣 �닔�젙
				if (imageFileName != null && imageFileName.length() != 0) {
					String originalFileName = articleMap.get("originalFileName");
					File srcFile = new File(ARTICLE_IMAGE_REPO + "\\" + "temp" + "\\" + imageFileName);
					File destDir = new File(ARTICLE_IMAGE_REPO + "\\" + articleNO);
					destDir.mkdirs();
					FileUtils.moveFileToDirectory(srcFile, destDir, true);
					
					File oldFile = new File(ARTICLE_IMAGE_REPO + "\\" + articleNO + "\\" + originalFileName);
					oldFile.delete();  //湲곗〈 �뙆�씪 �궘�젣
				}
				PrintWriter pw = response.getWriter();
				pw.print("<script>" + "  alert('湲��쓣 �닔�젙�븯���뒿�땲�떎.');" + " location.href='" + request.getContextPath()
						+ "/board/viewArticle.do?articleNO=" + articleNO + "';" + "</script>");
				return;
			} else if (action.equals("/removeArticle.do")) { //寃뚯떆湲� �궘�젣
				int articleNO = Integer.parseInt(request.getParameter("articleNO"));
				List<Integer> articleNOList = boardService.removeArticle(articleNO); //articleNo媛믪뿉 ���븳 湲��쓣 �궘�젣�븳 �썑 �궘�젣�맂 遺�紐④렇濡쒓� �옄�떇湲��쓽 articleNo紐⑸줉 媛��졇�샂
				for (int _articleNO : articleNOList) { //�궘�젣�맂 湲��뱾�쓽 �씠誘몄� ���옣�뤃�뜑 �궘�젣
					File imgDir = new File(ARTICLE_IMAGE_REPO + "\\" + _articleNO);
					if (imgDir.exists()) {
						FileUtils.deleteDirectory(imgDir);
					}
				}

				PrintWriter pw = response.getWriter();
				pw.print("<script>" + "  alert('湲��쓣 �궘�젣�븯���뒿�땲�떎.');" + " location.href='" + request.getContextPath()
						+ "/board/listArticles.do';" + "</script>");
				return;

			} else if (action.equals("/replyForm.do")) { //�떟湲� �옉�꽦 援ы쁽
				int parentNO = Integer.parseInt(request.getParameter("parentNO"));
				session = request.getSession();
				session.setAttribute("parentNO", parentNO);
				nextPage = "/board07/replyForm.jsp";
			} else if (action.equals("/addReply.do")) { //�떟湲� �쟾�넚
				session = request.getSession();
				int parentNO = (Integer) session.getAttribute("parentNO");
				session.removeAttribute("parentNO");
				Map<String, String> articleMap = upload(request, response);
				String title = articleMap.get("title");
				String content = articleMap.get("content");
				String imageFileName = articleMap.get("imageFileName");
				articleVO.setParentNO(parentNO);
				articleVO.setId("choco");
				articleVO.setTitle(title);
				articleVO.setContent(content);
				articleVO.setImageFileName(imageFileName);
				int articleNO = boardService.addReply(articleVO);
				if (imageFileName != null && imageFileName.length() != 0) {
					File srcFile = new File(ARTICLE_IMAGE_REPO + "\\" + "temp" + "\\" + imageFileName);
					File destDir = new File(ARTICLE_IMAGE_REPO + "\\" + articleNO);
					destDir.mkdirs();
					FileUtils.moveFileToDirectory(srcFile, destDir, true);
				}
				PrintWriter pw = response.getWriter();
				pw.print("<script>" + "  alert('�떟湲��쓣 異붽��뻽�뒿�땲�떎.');" + " location.href='" + request.getContextPath()
						+ "/board/viewArticle.do?articleNO="+articleNO+"';" + "</script>");
				return;
			}

			RequestDispatcher dispatch = request.getRequestDispatcher(nextPage);
			dispatch.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Map<String, String> upload(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Map<String, String> articleMap = new HashMap<String, String>();
		String encoding = "utf-8";
		File currentDirPath = new File(ARTICLE_IMAGE_REPO); //湲� �씠誘몄� ���옣 �뤃�뜑�뿉 ���빐 �뙆�씪 媛앹껜 �깮�꽦
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setRepository(currentDirPath);
		factory.setSizeThreshold(1024 * 1024);
		ServletFileUpload upload = new ServletFileUpload(factory);
		try {
			List items = upload.parseRequest(request);
			for (int i = 0; i < items.size(); i++) {
				FileItem fileItem = (FileItem) items.get(i);
				if (fileItem.isFormField()) {
					System.out.println(fileItem.getFieldName() + "=" + fileItem.getString(encoding));
					//�뙆�씪 �뾽濡쒕뱶濡� 媛숈씠 �쟾�넚�맂 �깉 湲� 愿��젴 留ㅺ컻蹂��닔瑜� Map�뿉 ���옣�븳 �썑 諛섑솚�븯怨�, �깉 湲�怨� 愿��젴�맂 title, content瑜� Map�뿉 ���옣
					articleMap.put(fileItem.getFieldName(), fileItem.getString(encoding));
				} else {
					System.out.println("�뙆�씪誘명꽣�씠由�:" + fileItem.getFieldName());
					System.out.println("�뙆�씪�씠由�:" + fileItem.getName());
					System.out.println("�뙆�씪�겕湲�:" + fileItem.getSize() + "bytes");
					
					//articleMap.put(fileItem.getFieldName(), fileItem.getName());
					//�뾽濡쒕뱶�븳 �뙆�씪�씠 議댁옱�븯�뒗 寃쎌슦 �뾽濡쒕뱶�븳 �뙆�씪�쓽 �뙆�씪 �씠由꾩쑝濡� ���옣�냼�뿉 �뾽濡쒕뱶
					if (fileItem.getSize() > 0) {
						int idx = fileItem.getName().lastIndexOf("\\");
						if (idx == -1) {
							idx = fileItem.getName().lastIndexOf("/");
						}

						String fileName = fileItem.getName().substring(idx + 1);
						System.out.println("占쏙옙占싹몌옙:" + fileName);
								articleMap.put(fileItem.getFieldName(), fileName);  //占싶쏙옙占시로뤄옙占쏙옙占쏙옙 占쏙옙占싸듸옙 占쏙옙占쏙옙占쏙옙 占쏙옙占� 占쏙옙占쏙옙 占쏙옙 map占쏙옙 占쏙옙占싹몌옙 占쏙옙占쏙옙);
						File uploadFile = new File(currentDirPath + "\\temp\\" + fileName); //泥⑤��븳 �뙆�씪�쓣 癒쇱� temp�뤃�뜑�뿉 �뾽濡쒕뱶
						fileItem.write(uploadFile);

					} // end if
				} // end if
			} // end for
		} catch (Exception e) {
			e.printStackTrace();
		}
		return articleMap;
	}

}
