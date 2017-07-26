package tiiehenry.os;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * ShellUtils
 * <ul>
 * <strong>Check root</strong>
 * <li>{@link ShellUtils#checkRootPermission()}</li>
 * </ul>
 * <ul>
 * <strong>Execte cmd</strong>
 * <li>{@link ShellUtils#execCmd(String, boolean)}</li>
 * <li>{@link ShellUtils#execCmd(String, boolean, boolean)}</li>
 * <li>{@link ShellUtils#execCmd(List, boolean)}</li>
 * <li>{@link ShellUtils#execCmd(List, boolean, boolean)}</li>
 * <li>{@link ShellUtils#execCmd(String[], boolean)}</li>
 * <li>{@link ShellUtils#execCmd(String[], boolean, boolean)}</li>
 * </ul>
 * 
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2013-5-16
 */
public class ShellUtil {

    public static final String COMMAND_SU       = "su";
    public static final String COMMAND_SH       = "sh";
    public static final String COMMAND_EXIT     = "exit\n";
    public static final String COMMAND_LINE_END = "\n";

    private ShellUtil() {
        throw new AssertionError();
    }

    /**
     * check whether has root permission
     * 
     * @return
     */
    public static boolean checkRootPermission() {
        return execCmd("echo root", true, false).result == 0;
    }

    /**
     * execute shell cmd, default return result msg
     * 
     * @param cmd cmd
     * @param isRoot whether need to run with root
     * @return
     * @see ShellUtils#execCmd(String[], boolean, boolean)
     */
    public static CmdResult execCmd(String cmd, boolean isRoot) {
        return execCmd(new String[] {cmd}, isRoot, true);
    }

    /**
     * execute shell cmds, default return result msg
     * 
     * @param cmds cmd list
     * @param isRoot whether need to run with root
     * @return
     * @see ShellUtils#execCmd(String[], boolean, boolean)
     */
    public static CmdResult execCmd(List<String> cmds, boolean isRoot) {
        return execCmd(cmds == null ? null : cmds.toArray(new String[] {}), isRoot, true);
    }

    /**
     * execute shell cmds, default return result msg
     * 
     * @param cmds cmd array
     * @param isRoot whether need to run with root
     * @return
     * @see ShellUtils#execCmd(String[], boolean, boolean)
     */
    public static CmdResult execCmd(String[] cmds, boolean isRoot) {
        return execCmd(cmds, isRoot, true);
    }

    /**
     * execute shell cmd
     * 
     * @param cmd cmd
     * @param isRoot whether need to run with root
     * @param isNeedResultMsg whether need result msg
     * @return
     * @see ShellUtils#execCmd(String[], boolean, boolean)
     */
    public static CmdResult execCmd(String cmd, boolean isRoot, boolean isNeedResultMsg) {
        return execCmd(new String[] {cmd}, isRoot, isNeedResultMsg);
    }

    /**
     * execute shell cmds
     * 
     * @param cmds cmd list
     * @param isRoot whether need to run with root
     * @param isNeedResultMsg whether need result msg
     * @return
     * @see ShellUtils#execCmd(String[], boolean, boolean)
     */
    public static CmdResult execCmd(List<String> cmds, boolean isRoot, boolean isNeedResultMsg) {
        return execCmd(cmds == null ? null : cmds.toArray(new String[] {}), isRoot, isNeedResultMsg);
    }

    /**
     * execute shell cmds
     * 
     * @param cmds cmd array
     * @param isRoot whether need to run with root
     * @param isNeedResultMsg whether need result msg
     * @return <ul>
     *         <li>if isNeedResultMsg is false, {@link CmdResult#successMsg} is null and
     *         {@link CmdResult#errorMsg} is null.</li>
     *         <li>if {@link CmdResult#result} is -1, there maybe some excepiton.</li>
     *         </ul>
     */
    public static CmdResult execCmd(String[] cmds, boolean isRoot, boolean isNeedResultMsg) {
        int result = -1;
        if (cmds == null || cmds.length == 0) {
            return new CmdResult(result, null, null);
        }

        Process process = null;
        BufferedReader successResult = null;
        BufferedReader errorResult = null;
        StringBuilder successMsg = null;
        StringBuilder errorMsg = null;

        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec(isRoot ? COMMAND_SU : COMMAND_SH);
            os = new DataOutputStream(process.getOutputStream());
            for (String cmd : cmds) {
                if (cmd == null) {
                    continue;
                }

                // donnot use os.writeBytes(commmand), avoid chinese charset error
                os.write(cmd.getBytes());
                os.writeBytes(COMMAND_LINE_END);
                os.flush();
            }
            os.writeBytes(COMMAND_EXIT);
            os.flush();

            result = process.waitFor();
            // get cmd result
            if (isNeedResultMsg) {
                successMsg = new StringBuilder();
                errorMsg = new StringBuilder();
                successResult = new BufferedReader(new InputStreamReader(process.getInputStream()));
                errorResult = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                String s;
                while ((s = successResult.readLine()) != null) {
                    successMsg.append(s);
                }
                while ((s = errorResult.readLine()) != null) {
                    errorMsg.append(s);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (successResult != null) {
                    successResult.close();
                }
                if (errorResult != null) {
                    errorResult.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (process != null) {
                process.destroy();
            }
        }
        return new CmdResult(result, successMsg == null ? null : successMsg.toString(), errorMsg == null ? null
                : errorMsg.toString());
    }

    /**
     * result of cmd
     * <ul>
     * <li>{@link CmdResult#result} means result of cmd, 0 means normal, else means error, same to excute in
     * linux shell</li>
     * <li>{@link CmdResult#successMsg} means success message of cmd result</li>
     * <li>{@link CmdResult#errorMsg} means error message of cmd result</li>
     * </ul>
     * 
     * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2013-5-16
     */
    public static class CmdResult {

        /** result of cmd **/
        public int    result;
        /** success message of cmd result **/
        public String successMsg;
        /** error message of cmd result **/
        public String errorMsg;

        public CmdResult(int result) {
            this.result = result;
        }

        public CmdResult(int result, String successMsg, String errorMsg) {
            this.result = result;
            this.successMsg = successMsg;
            this.errorMsg = errorMsg;
        }
    }
}
