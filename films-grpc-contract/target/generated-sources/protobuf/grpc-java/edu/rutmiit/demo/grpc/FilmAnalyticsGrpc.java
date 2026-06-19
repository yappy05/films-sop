package edu.rutmiit.demo.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 * <pre>
 * Сервисы
 * Unary RPC - один запрос это один ответ (типа аналог REST GET/POST)
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.66.0)",
    comments = "Source: film_analytics.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class FilmAnalyticsGrpc {

  private FilmAnalyticsGrpc() {}

  public static final java.lang.String SERVICE_NAME = "filmanalytics.FilmAnalytics";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<edu.rutmiit.demo.grpc.AnalyzeFilmRequest,
      edu.rutmiit.demo.grpc.FilmAnalysisResponse> getAnalyzeFilmMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "AnalyzeFilm",
      requestType = edu.rutmiit.demo.grpc.AnalyzeFilmRequest.class,
      responseType = edu.rutmiit.demo.grpc.FilmAnalysisResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<edu.rutmiit.demo.grpc.AnalyzeFilmRequest,
      edu.rutmiit.demo.grpc.FilmAnalysisResponse> getAnalyzeFilmMethod() {
    io.grpc.MethodDescriptor<edu.rutmiit.demo.grpc.AnalyzeFilmRequest, edu.rutmiit.demo.grpc.FilmAnalysisResponse> getAnalyzeFilmMethod;
    if ((getAnalyzeFilmMethod = FilmAnalyticsGrpc.getAnalyzeFilmMethod) == null) {
      synchronized (FilmAnalyticsGrpc.class) {
        if ((getAnalyzeFilmMethod = FilmAnalyticsGrpc.getAnalyzeFilmMethod) == null) {
          FilmAnalyticsGrpc.getAnalyzeFilmMethod = getAnalyzeFilmMethod =
              io.grpc.MethodDescriptor.<edu.rutmiit.demo.grpc.AnalyzeFilmRequest, edu.rutmiit.demo.grpc.FilmAnalysisResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "AnalyzeFilm"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  edu.rutmiit.demo.grpc.AnalyzeFilmRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  edu.rutmiit.demo.grpc.FilmAnalysisResponse.getDefaultInstance()))
              .setSchemaDescriptor(new FilmAnalyticsMethodDescriptorSupplier("AnalyzeFilm"))
              .build();
        }
      }
    }
    return getAnalyzeFilmMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static FilmAnalyticsStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<FilmAnalyticsStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<FilmAnalyticsStub>() {
        @java.lang.Override
        public FilmAnalyticsStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new FilmAnalyticsStub(channel, callOptions);
        }
      };
    return FilmAnalyticsStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static FilmAnalyticsBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<FilmAnalyticsBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<FilmAnalyticsBlockingStub>() {
        @java.lang.Override
        public FilmAnalyticsBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new FilmAnalyticsBlockingStub(channel, callOptions);
        }
      };
    return FilmAnalyticsBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static FilmAnalyticsFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<FilmAnalyticsFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<FilmAnalyticsFutureStub>() {
        @java.lang.Override
        public FilmAnalyticsFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new FilmAnalyticsFutureStub(channel, callOptions);
        }
      };
    return FilmAnalyticsFutureStub.newStub(factory, channel);
  }

  /**
   * <pre>
   * Сервисы
   * Unary RPC - один запрос это один ответ (типа аналог REST GET/POST)
   * </pre>
   */
  public interface AsyncService {

    /**
     * <pre>
     * Анализирует фильм и возвращает вычисленные метрики
     * </pre>
     */
    default void analyzeFilm(edu.rutmiit.demo.grpc.AnalyzeFilmRequest request,
        io.grpc.stub.StreamObserver<edu.rutmiit.demo.grpc.FilmAnalysisResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getAnalyzeFilmMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service FilmAnalytics.
   * <pre>
   * Сервисы
   * Unary RPC - один запрос это один ответ (типа аналог REST GET/POST)
   * </pre>
   */
  public static abstract class FilmAnalyticsImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return FilmAnalyticsGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service FilmAnalytics.
   * <pre>
   * Сервисы
   * Unary RPC - один запрос это один ответ (типа аналог REST GET/POST)
   * </pre>
   */
  public static final class FilmAnalyticsStub
      extends io.grpc.stub.AbstractAsyncStub<FilmAnalyticsStub> {
    private FilmAnalyticsStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected FilmAnalyticsStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new FilmAnalyticsStub(channel, callOptions);
    }

    /**
     * <pre>
     * Анализирует фильм и возвращает вычисленные метрики
     * </pre>
     */
    public void analyzeFilm(edu.rutmiit.demo.grpc.AnalyzeFilmRequest request,
        io.grpc.stub.StreamObserver<edu.rutmiit.demo.grpc.FilmAnalysisResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getAnalyzeFilmMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service FilmAnalytics.
   * <pre>
   * Сервисы
   * Unary RPC - один запрос это один ответ (типа аналог REST GET/POST)
   * </pre>
   */
  public static final class FilmAnalyticsBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<FilmAnalyticsBlockingStub> {
    private FilmAnalyticsBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected FilmAnalyticsBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new FilmAnalyticsBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * Анализирует фильм и возвращает вычисленные метрики
     * </pre>
     */
    public edu.rutmiit.demo.grpc.FilmAnalysisResponse analyzeFilm(edu.rutmiit.demo.grpc.AnalyzeFilmRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getAnalyzeFilmMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service FilmAnalytics.
   * <pre>
   * Сервисы
   * Unary RPC - один запрос это один ответ (типа аналог REST GET/POST)
   * </pre>
   */
  public static final class FilmAnalyticsFutureStub
      extends io.grpc.stub.AbstractFutureStub<FilmAnalyticsFutureStub> {
    private FilmAnalyticsFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected FilmAnalyticsFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new FilmAnalyticsFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * Анализирует фильм и возвращает вычисленные метрики
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<edu.rutmiit.demo.grpc.FilmAnalysisResponse> analyzeFilm(
        edu.rutmiit.demo.grpc.AnalyzeFilmRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getAnalyzeFilmMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_ANALYZE_FILM = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_ANALYZE_FILM:
          serviceImpl.analyzeFilm((edu.rutmiit.demo.grpc.AnalyzeFilmRequest) request,
              (io.grpc.stub.StreamObserver<edu.rutmiit.demo.grpc.FilmAnalysisResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getAnalyzeFilmMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              edu.rutmiit.demo.grpc.AnalyzeFilmRequest,
              edu.rutmiit.demo.grpc.FilmAnalysisResponse>(
                service, METHODID_ANALYZE_FILM)))
        .build();
  }

  private static abstract class FilmAnalyticsBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    FilmAnalyticsBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return edu.rutmiit.demo.grpc.FilmAnalyticsOuterClass.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("FilmAnalytics");
    }
  }

  private static final class FilmAnalyticsFileDescriptorSupplier
      extends FilmAnalyticsBaseDescriptorSupplier {
    FilmAnalyticsFileDescriptorSupplier() {}
  }

  private static final class FilmAnalyticsMethodDescriptorSupplier
      extends FilmAnalyticsBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    FilmAnalyticsMethodDescriptorSupplier(java.lang.String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (FilmAnalyticsGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new FilmAnalyticsFileDescriptorSupplier())
              .addMethod(getAnalyzeFilmMethod())
              .build();
        }
      }
    }
    return result;
  }
}
