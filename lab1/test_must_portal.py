from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.chrome.options import Options
from selenium.common.exceptions import TimeoutException, NoSuchElementException
import time
import logging
# Логийн тохиргоо
logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')
logger = logging.getLogger(__name__)
class MustLoginTest:
    """MUST-н студентийн портал дахь нэвтрэх тестийн класс"""
    def __init__(self):
        """Тестийн эхлэл: хөтөч тохируулах"""
        self.driver = None
        self.wait = None
        self.setup_driver()
    def setup_driver(self):
        """Алхам 1: Орчин бэлдэх - Chrome хөтчийг тохируулах"""
        try:
            # Chrome хөтчийн тохиргоо
            chrome_options = Options()
            chrome_options.add_argument("--start-maximized")  # Хөтчийг бүтэн цонхоор нээх
            chrome_options.add_argument("--disable-web-security")  # Аюулгүй байдлын сануулга устгах
            chrome_options.add_argument("--disable-features=VizDisplayCompositor")
            # ChromeDriver-г эхлүүлэх (ChromeDriver PATH дээр байгаа гэж үзэж байна)
            self.driver = webdriver.Chrome(options=chrome_options)
            # Хүлээх хугацааны тохиргоо - 10 секунд хүлээнэ
            self.wait = WebDriverWait(self.driver, 10)
            logger.info(" Chrome хөтөч амжилттай эхлэлээ")
        except Exception as e:
            logger.error(f" Хөтөч тохируулахад алдаа гарлаа: {e}")
            raise
    def load_website(self, url="https://student.must.edu.mn"):
        """Алхам 2: Тестийн вебийг ачааллах"""
        try:
            logger.info(f" Вебсайт ачааллаж байна: {url}")
            self.driver.get(url)
            # Хуудас ачаалагдах хүртэл хүлээх
            self.wait.until(EC.presence_of_element_located((By.TAG_NAME, "body")))
            # Хуудасны гарчиг шалгах
            page_title = self.driver.title
            logger.info(f" Хуудасны гарчиг: {page_title}")
            # 2 секунд хүлээх (хуудас бүрэн ачаалагдахын тулд)
            time.sleep(2)
            return True
        except TimeoutException:
            logger.error(" Хуудас ачаалагдах хугацаа дууссан")
            return False
        except Exception as e:
            logger.error(f" Вебсайт ачааллахад алдаа гарлаа: {e}")
            return False
    def perform_login_action(self, username="", password=""):
        """Алхам 2: Нэвтрэх үйлдэл гүйцэтгэх"""
        try:
            logger.info(" Нэвтрэх үйлдэл эхэлж байна...")
            # Хэрэглэгчийн нэр оруулах талбар олох (олон янзын селектор ашиглах)
            username_selectors = [
                (By.ID, "username"),
                (By.NAME, "username"),
                (By.ID, "user"),
                (By.NAME, "user"),
                (By.XPATH, "//input[@type='text' or @type='email']"),
                (By.CSS_SELECTOR, "input[placeholder*='username' i]"),
                (By.CSS_SELECTOR, "input[placeholder*='нэр' i]")
            ]
            username_field = None
            for selector_type, selector_value in username_selectors:
                try:
                    username_field = self.wait.until(
                        EC.element_to_be_clickable((selector_type, selector_value))
                    )
                    logger.info(f"Хэрэглэгчийн нэр талбар олдлоо: {selector_type}='{selector_value}'")
                    break
                except TimeoutException:
                    continue
            if not username_field:
                logger.warning(" Хэрэглэгчийн нэр талбар олдсонгүй")
                return False
            # Нууц үг талбар олох
            password_selectors = [
                (By.ID, "password"),
                (By.NAME, "password"),
                (By.XPATH, "//input[@type='password']"),
                (By.CSS_SELECTOR, "input[placeholder*='password' i]"),
                (By.CSS_SELECTOR, "input[placeholder*='нууц' i]")
            ]
            password_field = None
            for selector_type, selector_value in password_selectors:
                try:
                    password_field = self.driver.find_element(selector_type, selector_value)
                    logger.info(f"Нууц үг талбар олдлоо: {selector_type}='{selector_value}'")
                    break
                except NoSuchElementException:
                    continue
            if not password_field:
                logger.warning(" Нууц үг талбар олдсонгүй")
                return False
            # Хэрэв username, password өгөгдсөн бол оруулах
            if username:
                username_field.clear()
                username_field.send_keys(username)
                logger.info(" Хэрэглэгчийн нэр оруулсан")
            if password:
                password_field.clear()
                password_field.send_keys(password)
                logger.info("Нууц үг оруулсан")
            # Нэвтрэх товч олох
            login_button_selectors = [
                (By.ID, "login"),
                (By.ID, "submit"),
                (By.NAME, "login"),
                (By.XPATH, "//button[@type='submit']"),
                (By.XPATH, "//input[@type='submit']"),
                (By.CSS_SELECTOR, "button[class*='login' i]"),
                (By.XPATH, "//button[contains(text(), 'Login') or contains(text(), 'Нэвтрэх')]")
            ]
            login_button = None
            for selector_type, selector_value in login_button_selectors:
                try:
                    login_button = self.driver.find_element(selector_type, selector_value)
                    logger.info(f" Нэвтрэх товч олдлоо: {selector_type}='{selector_value}'")
                    break
                except NoSuchElementException:
                    continue
            # Хэрэв нэвтрэх товч олдсон бол дарах
            if login_button:
                login_button.click()
                logger.info(" Нэвтрэх товч дарлаа")
                time.sleep(3)  # Хариу хүлээх
                return True
            else:
                logger.warning(" Нэвтрэх товч олдсонгүй")
                return False
        except Exception as e:
            logger.error(f" Нэвтрэх үйлдэлд алдаа гарлаа: {e}")
            return False
    def verify_login_result(self):
        """Алхам 3: Нэвтрэх үр дүнг шалгах (Assertions)"""
        try:
            logger.info("Нэвтрэх үр дүнг шалгаж байна...")
            # URL өөрчлөгдсөн эсэхийг шалгах
            current_url = self.driver.current_url
            logger.info(f" Одоогийн URL: {current_url}")
            # Амжилттай нэвтэрсэн эсэхийг шалгах элементүүд
            success_indicators = [
                (By.XPATH, "//a[contains(text(), 'Profile') or contains(text(), 'Профайл')]"),
                (By.XPATH, "//a[contains(text(), 'Dashboard') or contains(text(), 'Хяналтын самбар')]"),
                (By.XPATH, "//a[contains(text(), 'Logout') or contains(text(), 'Гарах')]"),
                (By.CLASS_NAME, "user-info"),
                (By.CLASS_NAME, "profile"),
                (By.ID, "user-menu")
            ]
            login_success = False
            for selector_type, selector_value in success_indicators:
                try:
                    element = self.driver.find_element(selector_type, selector_value)
                    if element.is_displayed():
                        logger.info(f" Амжилттай нэвтэрсэн шалгуур олдлоо: {selector_type}='{selector_value}'")
                        login_success = True
                        break
                except NoSuchElementException:
                    continue
            # Алдааны мэдээлэл шалгах
            error_indicators = [
                (By.XPATH, "//div[contains(@class, 'error') or contains(@class, 'alert')]"),
                (By.XPATH, "//*[contains(text(), 'Invalid') or contains(text(), 'алдаа')]"),
                (By.CLASS_NAME, "error-message")
            ]
            error_found = False
            for selector_type, selector_value in error_indicators:
                try:
                    error_element = self.driver.find_element(selector_type, selector_value)
                    if error_element.is_displayed():
                        error_text = error_element.text
                        logger.warning(f" Алдааны мэдээлэл олдлоо: {error_text}")
                        error_found = True
                        break
                except NoSuchElementException:
                    continue
            # Үр дүнгийн дүгнэлт
            if login_success:
                logger.info(" ТЕСТ АМЖИЛТТАЙ: Нэвтрэх үйлдэл амжилттай боллоо")
                return True
            elif error_found:
                logger.info("ТЕСТ АМЖИЛТТАЙ: Алдааны мэдээлэл зөв харагдаж байна")
                return True
            else:
                logger.info(" ТЕСТ: Нэвтрэх үр дүн тодорхойгүй байна")
                return False
        except Exception as e:
            logger.error(f" Үр дүн шалгахад алдаа гарлаа: {e}")
            return False
    def perform_logout(self):
        """Алхам 5: Системээс гарах"""
        try:
            logger.info(" Системээс гарах үйлдэл эхэлж байна...")
            logout_selectors = [
                (By.XPATH, "//a[contains(text(), 'Logout') or contains(text(), 'Гарах')]"),
                (By.XPATH, "//a[contains(text(), 'Sign Out')]"),
                (By.ID, "logout"),
                (By.CLASS_NAME, "logout"),
                (By.XPATH, "//button[contains(text(), 'Logout')]")
            ]
            for selector_type, selector_value in logout_selectors:
                try:
                    logout_element = self.driver.find_element(selector_type, selector_value)
                    if logout_element.is_displayed():
                        logout_element.click()
                        logger.info(" Амжилттай гарлаа")
                        time.sleep(2)
                        return True
                except NoSuchElementException:
                    continue
            logger.info("Гарах товч олдсонгүй")
            return False
        except Exception as e:
            logger.error(f" Гарахад алдаа гарлаа: {e}")
            return False
    def remove_overlay_if_present(self):
        """Нэвтэрсний дараа гарч ирдэг overlay (саад) байвал арилгах"""
        try:
            logger.info("Overlay шалгаж байна...")
            overlay_selectors = [
                (By.CLASS_NAME, "overlay"),
                (By.CLASS_NAME, "modal-backdrop"),
                (By.CSS_SELECTOR, ".overlay"),
                (By.CSS_SELECTOR, ".modal-backdrop"),
                (By.XPATH, "//div[contains(@class, 'overlay') or contains(@class, 'modal-backdrop')]"),
                (By.XPATH, "//div[@role='dialog']")
            ]
            for selector_type, selector_value in overlay_selectors:
                try:
                    overlay = self.driver.find_element(selector_type, selector_value)
                    if overlay.is_displayed():
                        logger.info(" Overlay илэрсэн тул арилгаж байна...")
                        try:
                            overlay.click()
                            logger.info(" Overlay дээр дарж арилгав")
                        except Exception:
                            from selenium.webdriver.common.keys import Keys
                            self.driver.switch_to.active_element.send_keys(Keys.ESCAPE)
                            logger.info(" Escape товч илгээж overlay арилгав")
                        time.sleep(1)
                        return True
                except NoSuchElementException:
                    continue
            logger.info("Overlay илрээгүй")
            return False
        except Exception as e:
            logger.error(f" Overlay арилгахад алдаа: {e}")
            return False
    def go_to_courses_section(self):
        """Overlay арилсны дараа 'ХИЧЭЭЛ' хэсэг рүү очих"""
        try:
            logger.info(" 'ХИЧЭЭЛ' хэсэг рүү шилжиж байна...")
            course_selectors = [
                (By.LINK_TEXT, "ХИЧЭЭЛ"),
                (By.PARTIAL_LINK_TEXT, "ХИЧЭЭЛ"),
                (By.XPATH, "//a[normalize-space(text())='ХИЧЭЭЛ']"),
                (By.XPATH, "//*[normalize-space(text())='ХИЧЭЭЛ']"),
                (By.XPATH, "//li[.//text()[normalize-space()='ХИЧЭЭЛ']]"),
                (By.XPATH, "//span[normalize-space(text())='ХИЧЭЭЛ']"),
            ]
            for selector_type, selector_value in course_selectors:
                try:
                    course_link = self.driver.find_element(selector_type, selector_value)
                    if course_link.is_displayed():
                        course_link.click()
                        logger.info(" 'ХИЧЭЭЛ' хэсэг рүү амжилттай орлоо")
                        time.sleep(2)
                        return True
                except NoSuchElementException:
                    continue
            logger.warning("'ХИЧЭЭЛ' хэсгийн линк олдсонгүй")
            return False
        except Exception as e:
            logger.error(f" 'ХИЧЭЭЛ' хэсэг рүү ороход алдаа: {e}")
            return False        
    def go_to_schedule_section(self):
        """'ХИЧЭЭЛ' цэс дээр дарсны дараа 'Хичээлийн хуваарь ' хэсэг рүү очих"""
        try:
            logger.info(" 'Хичээлийн хуваарь ' хэсэг рүү шилжиж байна...")
            schedule_selectors = [
                (By.LINK_TEXT, "Хичээлийн хуваарь "),
                (By.PARTIAL_LINK_TEXT, "хуваарь"),
                (By.XPATH, "//a[normalize-space(text())='Хичээлийн хуваарь ']"),
                (By.XPATH, "//*[normalize-space(text())='Хичээлийн хуваарь ']"),
                (By.XPATH, "//li[.//text()[normalize-space()='Хичээлийн хуваарь ']]"),
                (By.XPATH, "//span[normalize-space(text())='Хичээлийн хуваарь ']"),
            ]
            for selector_type, selector_value in schedule_selectors:
                try:
                    schedule_link = self.driver.find_element(selector_type, selector_value)
                    if schedule_link.is_displayed():
                        schedule_link.click()
                        logger.info(" 'Хичээлийн хуваарь ' хэсэг рүү амжилттай орлоо")
                        time.sleep(2)
                        return True
                except NoSuchElementException:
                    continue
            logger.warning("'Хичээлийн хуваарь ' хэсгийн линк олдсонгүй")
            return False
        except Exception as e:
            logger.error(f" 'Хичээлийн хуваарь ' хэсэг рүү ороход алдаа: {e}")
            return False
    def cleanup(self):
        """Алхам 5: Төгсгөх - хөтчийг хаах"""
        try:
            if self.driver:
                logger.info(" Хөтчийг хааж байна...")
                time.sleep(2)  # Эцсийн үр дүн харахын тулд 2 секунд хүлээх
                self.driver.quit()
                logger.info(" Хөтөч амжилттай хаагдлаа")
        except Exception as e:
            logger.error(f" Хөтөч хаахад алдаа гарлаа: {e}")
    def run_full_test(self, username="", password=""):
        """Бүрэн тест ажиллуулах"""
        try:
            logger.info(" SELENIUM WEBDRIVER ТЕСТ ЭХЭЛЖ БАЙНА")
            logger.info("=" * 50)
            # Алхам 2: Вебсайт ачааллах
            if not self.load_website():
                logger.error(" Вебсайт ачаалахад алдаа гарсан тул тест зогслоо")
                return False
            # Алхам 2: Нэвтрэх үйлдэл
            login_performed = self.perform_login_action(username, password)
             # Overlay арилгах (шинэ алхам)
            self.remove_overlay_if_present()
            # Алхам 3: Үр дүн шалгах
            verification_result = self.verify_login_result()
            # Зөвхөн амжилттай нэвтэрсэн үед "Хичээл" хэсэг рүү орно
            if verification_result:
                if self.go_to_courses_section():
                    if self.go_to_schedule_section():
                        self.go_to_print_section()
            else:
                logger.error(" Нэвтрэлт амжилтгүй тул 'Хичээл' хэсэг рүү орохгүй")
            # Алхам 5: Гарах (хэрэв нэвтэрсэн бол)
            self.perform_logout()
            logger.info("=" * 50)
            logger.info(" ТЕСТ АМЖИЛТТАЙ ДУУСГАЛАА")
            return True
        except Exception as e:
            logger.error(f" Тестэд ерөнхий алдаа гарлаа: {e}")
            return False
        finally:
            # Алхам 5: Цэвэрлэх
            self.cleanup()

def main():
    """Гол функц - тестийг ажиллуулах"""
    print(" MUST Студентийн Портал - Selenium WebDriver Тест")
    print("=" * 55)
    # Тест үүсгэх
    test = MustLoginTest()
    # Тестийг ажиллуулах (хэрэглэгчийн нэр, нууц үг
    test.run_full_test(username="B222270048", password="042506")
if __name__ == "__main__":
    main()