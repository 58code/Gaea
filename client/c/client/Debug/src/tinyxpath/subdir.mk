################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
CPP_SRCS += \
../src/tinyxpath/action_store.cpp \
../src/tinyxpath/htmlutil.cpp \
../src/tinyxpath/lex_util.cpp \
../src/tinyxpath/main.cpp \
../src/tinyxpath/node_set.cpp \
../src/tinyxpath/tinystr.cpp \
../src/tinyxpath/tinyxml.cpp \
../src/tinyxpath/tinyxmlerror.cpp \
../src/tinyxpath/tinyxmlparser.cpp \
../src/tinyxpath/tokenlist.cpp \
../src/tinyxpath/xml_util.cpp \
../src/tinyxpath/xpath_expression.cpp \
../src/tinyxpath/xpath_processor.cpp \
../src/tinyxpath/xpath_stack.cpp \
../src/tinyxpath/xpath_static.cpp \
../src/tinyxpath/xpath_stream.cpp \
../src/tinyxpath/xpath_syntax.cpp 

OBJS += \
./src/tinyxpath/action_store.o \
./src/tinyxpath/htmlutil.o \
./src/tinyxpath/lex_util.o \
./src/tinyxpath/main.o \
./src/tinyxpath/node_set.o \
./src/tinyxpath/tinystr.o \
./src/tinyxpath/tinyxml.o \
./src/tinyxpath/tinyxmlerror.o \
./src/tinyxpath/tinyxmlparser.o \
./src/tinyxpath/tokenlist.o \
./src/tinyxpath/xml_util.o \
./src/tinyxpath/xpath_expression.o \
./src/tinyxpath/xpath_processor.o \
./src/tinyxpath/xpath_stack.o \
./src/tinyxpath/xpath_static.o \
./src/tinyxpath/xpath_stream.o \
./src/tinyxpath/xpath_syntax.o 

CPP_DEPS += \
./src/tinyxpath/action_store.d \
./src/tinyxpath/htmlutil.d \
./src/tinyxpath/lex_util.d \
./src/tinyxpath/main.d \
./src/tinyxpath/node_set.d \
./src/tinyxpath/tinystr.d \
./src/tinyxpath/tinyxml.d \
./src/tinyxpath/tinyxmlerror.d \
./src/tinyxpath/tinyxmlparser.d \
./src/tinyxpath/tokenlist.d \
./src/tinyxpath/xml_util.d \
./src/tinyxpath/xpath_expression.d \
./src/tinyxpath/xpath_processor.d \
./src/tinyxpath/xpath_stack.d \
./src/tinyxpath/xpath_static.d \
./src/tinyxpath/xpath_stream.d \
./src/tinyxpath/xpath_syntax.d 


# Each subdirectory must supply rules for building sources it contributes
src/tinyxpath/%.o: ../src/tinyxpath/%.cpp
	@echo 'Building file: $<'
	@echo 'Invoking: Cross G++ Compiler'
	g++ -O0 -g3 -Wall -c -fmessage-length=0 -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@:%.o=%.d)" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


