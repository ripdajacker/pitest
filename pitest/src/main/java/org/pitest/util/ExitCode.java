/*
 * Copyright 2010 Henry Coles
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, 
 * software distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and limitations under the License. 
 */
package org.pitest.util;

public enum ExitCode {

  OK(0, ErrorType.NONE), OUT_OF_MEMORY(11, ErrorType.HANDLED), FORCED_EXIT(12,
      ErrorType.HANDLED), UNKNOWN_ERROR(13, ErrorType.UNHANDLED), TIMEOUT(14,
      ErrorType.HANDLED);

  private static enum ErrorType {
    NONE, HANDLED, UNHANDLED;
  }

  private final int       code;
  private final ErrorType errorType;

  private ExitCode(final int code, final ErrorType errorType) {
    this.code = code;
    this.errorType = errorType;
  }

  public int getCode() {
    return this.code;
  }

  public static ExitCode fromCode(final int code) {
    for (final ExitCode each : values()) {
      if (each.getCode() == code) {
        return each;
      }
    }

    return UNKNOWN_ERROR;
  }

  public boolean isOk() {
    return this.errorType.equals(ErrorType.NONE);
  }

  public boolean isHandledError() {
    return this.errorType.equals(ErrorType.HANDLED);
  }

  public boolean isUnhandledError() {
    return this.errorType.equals(ErrorType.UNHANDLED);
  }

}
