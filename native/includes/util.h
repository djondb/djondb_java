/*
 * File:   util.h
 * Author: cross
 *
 * Created on July 7, 2010, 1:33 PM
 */

#ifndef _UTIL_H
#define	_UTIL_H

#include "datetime.h"
#include "dtime.h"
#include "fileutil.h"
#include "stringfunctions.h"
#include "errorhandle.h"
#include "version.h"
#include "logger.h"
#include "threads.h"
#include "settings.h"
#include "circular_queue.h"
#include "djon_error_codes.h"
#include "defs.h"

#include <string>
#include <vector>
#include <map>
#ifndef WINDOWS
#include <time.h>
#endif

class DjondbException: public std::exception {
	public:
		DjondbException(int code, const char* error);
		DjondbException(const DjondbException& orig);
		virtual const char* what() const throw();
		int errorCode() const;

	private:
		int _errorCode;
		const char* _errorMessage;
};

#endif	/* _UTIL_H */

